package com.shidan.ark.mock.core.plugins.dubbo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.jvm.sandbox.api.ProcessController;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.EventListener;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceAdapterListener;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.google.common.collect.Lists;
import com.shidan.ark.mock.core.cache.RulesCache;
import com.shidan.ark.mock.core.enums.MatchTypeEnum;
import com.shidan.ark.mock.core.handler.MockLogReportHandler;
import com.shidan.ark.mock.core.handler.QueueGenerationService;
import com.shidan.ark.mock.core.listener.InvocationListener;
import com.shidan.ark.mock.core.model.*;
import com.shidan.ark.mock.core.plugins.AbstractMockPluginAdapter;
import com.shidan.ark.mock.core.plugins.MockPlugin;
import com.shidan.ark.mock.core.plugins.MockType;
import com.shidan.ark.mock.module.Constants;
import com.shidan.ark.mock.util.LogUtil;
import org.kohsuke.MetaInfServices;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author ：shidan
 */
@MetaInfServices(MockPlugin.class)
public class DubboPlugin extends AbstractMockPluginAdapter {

    Logger log = LogUtil.getLogger(DubboPlugin.class);


    @Override
    public MockType getType() {
        return MockType.DUBBO;
    }

    @Override
    public String identity() {
        return "dubbo-consumer";
    }

    @Override
    protected List<EnhanceModel> getEnhanceModels() {
        log.info("dubbo插件，getEnhanceModels,start...");
        EnhanceModel onResponse = EnhanceModel.builder().classPattern("com.alibaba.dubbo.rpc.filter.ConsumerContextFilter")
                .methodPatterns(EnhanceModel.MethodPattern.transform("invoke"))
                .watchTypes(Event.Type.BEFORE)
                .build();
        log.info("dubbo插件，getEnhanceModels,end...");
        return Lists.newArrayList(onResponse);
    }

    @Override
    protected EventListener getEventListener(InvocationListener listener) {
        log.info("dubbo插件，getEventListener start...");
        AdviceAdapterListener adviceAdapterListener = new AdviceAdapterListener(new AdviceListener() {
            @Override
            protected void before(Advice advice) throws Throwable {
                String parameterArrayStr = JSON.toJSONString(advice.getParameterArray());
                JSONArray parameterArray = JSONArray.parseArray(parameterArrayStr);
                String inter = parameterArray.getJSONObject(0).getString("interface");
                String method = parameterArray.getJSONObject(1).getString("methodName");
                //入参数组
                JSONArray argumentsArray = parameterArray.getJSONObject(1).getJSONArray("arguments");
                String arguments = argumentsArray.toJSONString();

                //入参类型
                JSONArray parameterTypesArray = parameterArray.getJSONObject(1).getJSONArray("parameterTypes");

                log.info("dubbo插件拦截到invoke，" + parameterArrayStr);
                List<Rule> dubboConsumerRules = RulesCache.get(identity());
                for (Rule rule : dubboConsumerRules) {
                    //判断接口、方法匹配
                    log.info("dubbo插件判断规则，" + rule.toString());
                    if (rule.getPath().equals(inter) && rule.getMethod().equals(method)) {
                        //正则匹配
                        log.info("dubbo插件判断规则，" + rule.toString());
                        if (rule.getMatchType().equals(MatchTypeEnum.Regex.getCode()) && arguments.matches(rule.getMatchRule())) {
                            log.info("dubbo插件判断规则，正则表达式命中");
                            ClassLoader classLoader = advice.getLoader();
                            // todo,兼容各dubbo版本
                            Class clz = classLoader.loadClass("com.alibaba.dubbo.rpc.RpcResult");
                            Object returnObject = clz.newInstance();
                            // todo,data是数组的情况待兼容
                            JSONObject data = JSONObject.parseObject(rule.getOutputTemp()).getJSONObject("data");
                            Class returnClz = classLoader.loadClass(JSONObject.parseObject(rule.getOutputTemp()).getString("type"));
                            Object returnData = data.toJavaObject(returnClz);


                            Field field = clz.getDeclaredField("result");
                            field.setAccessible(true);
                            field.set(returnObject, returnData);


                            //todo，上报mock数据，抽成公共事件上报
                            ReportModel reportModel = new ReportModel();
                            reportModel.setApp(ApplicationModel.instance().getAppName());
                            reportModel.setEnvironment(ApplicationModel.instance().getEnvironment());
                            reportModel.setIp(ApplicationModel.instance().getHost());
                            reportModel.setMethod(method);
                            reportModel.setPath(inter);
                            reportModel.setMockType(getType().name());
                            JSONObject realInput = new JSONObject().fluentPut("arguments", argumentsArray).fluentPut("parameterTypes", parameterTypesArray);
                            reportModel.setRealInput(realInput.toJSONString());
                            reportModel.setRealOutput(data.toJSONString());
                            reportModel.setTempId(rule.getTempId());
                            reportModel.setVersion(Constants.VERSION); //模块版本
                            reportModel.setTempSnapshot(JSON.toJSONString(rule));

                            log.info("添加上报数据");
                            QueueGenerationService.addData(new MockLogReportHandler(reportModel));


                            ProcessController.returnImmediately(returnObject);
                        }
                        //todo,其他匹配方式
                    }
                }
            }

            @Override
            protected void after(Advice advice) throws Throwable {

            }
        });
        log.info("dubbo插件，getEventListener end...");
        return adviceAdapterListener;
    }
}
