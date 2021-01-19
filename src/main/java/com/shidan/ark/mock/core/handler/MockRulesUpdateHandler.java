package com.shidan.ark.mock.core.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shidan.ark.mock.core.cache.RulesCache;
import com.shidan.ark.mock.core.model.Rule;
import com.shidan.ark.mock.core.enums.MockPluginEnum;
import com.shidan.ark.mock.module.Constants;
import com.shidan.ark.mock.util.HttpUtil;
import com.shidan.ark.mock.util.LogUtil;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * @author ：shidan
 */
public class MockRulesUpdateHandler {

    Logger log = LogUtil.getLogger(MockRulesUpdateHandler.class);


    private static final long FREQUENCY = 3 * 60;  //3分钟更新一次

    private final static String MOCK_RULES_DOMAIN = Constants.CONSOLE_ADDRESS + Constants.MOCK_RULES_PATH;

    private AtomicBoolean initialize = new AtomicBoolean(false);

    private static ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("rules-pool-%d").daemon(true).build());


    public synchronized void start() {
        log.info("心跳上报定时任务(3min间隔)，启动");
        if (initialize.compareAndSet(false, true)) {
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        updateRules();
                    } catch (Exception e) {
                        log.info("规则更新异常，" + e.getLocalizedMessage());
                    }
                }
            }, 0, FREQUENCY, TimeUnit.SECONDS);
        }
    }


    public synchronized void updateRules() {
        Arrays.stream(MockPluginEnum.values()).forEach(item -> {
            Map<String, String> params = new HashMap<String, String>();
            params.put("status", "1");
            params.put("mockType", item.getMockTypeCode());
            // 获取规则
            HttpUtil.Resp resp = HttpUtil.doPostJson(MOCK_RULES_DOMAIN, params);
            if (resp.getCode() == 200) {
                log.info("插件" + item.getIdentity() + "规则查询返回" + resp.getBody());
                JSONObject jBody = JSONObject.parseObject(resp.getBody());
                List rules = new ArrayList<Rule>();
                if ((Boolean) jBody.get("success")) {
                    JSONObject jData = (JSONObject) jBody.get("data");
                    JSONArray jRecords = (JSONArray) jData.get("records");
                    jRecords.forEach(record -> {
                        JSONObject jRecord = (JSONObject) record;
                        rules.add(jRecord.toJavaObject(Rule.class));
                    });
                }
                // todo,按修改时间倒序排
                String identity = MockPluginEnum.getIdentityByCode(item.getMockTypeCode());
                RulesCache.put(identity, rules);
            }
            log.info(item.getIdentity() + "缓存最新规则" + RulesCache.get(item.getIdentity()).toString());
        });
        log.info("所有插件，mock规则更新成功");
    }

}
