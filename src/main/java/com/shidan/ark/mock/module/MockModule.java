package com.shidan.ark.mock.module;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.api.resource.ConfigInfo;
import com.alibaba.jvm.sandbox.api.resource.ModuleController;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleManager;
import com.shidan.ark.mock.core.exception.PluginLifeCycleException;
import com.shidan.ark.mock.core.handler.MockRulesUpdateHandler;
import com.shidan.ark.mock.core.handler.HeartbeatHandler;
import com.shidan.ark.mock.core.listener.InvocationListener;
import com.shidan.ark.mock.core.model.MockConfig;
import com.shidan.ark.mock.core.plugins.MockPlugin;

import com.shidan.ark.mock.util.LogUtil;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import java.util.ServiceLoader;
import java.util.logging.Logger;

/**
 * @author ：shidan
 */
@MetaInfServices(Module.class)
@Information(id = Constants.MODULE_ID, author = "shidan", version = Constants.VERSION)
public class MockModule implements Module, ModuleLifecycle {

//    private final static Logger log = LoggerFactory.getLogger(MockModule.class);

    private final static Logger log = LogUtil.getLogger(MockModule.class);

    @Resource
    private ModuleEventWatcher eventWatcher;

    @Resource
    private ModuleManager moduleManager;

    @Resource
    private ConfigInfo configInfo;

    @Resource
    private ModuleController moduleController;


    private HeartbeatHandler heartbeatHandler;

    private MockRulesUpdateHandler updateMockRulesHandler =new MockRulesUpdateHandler();

    private InvocationListener invocationListener;


    @Override
    public void onLoad() throws Throwable {
        log.info("mock插件onLoad，start...");
        moduleController.active();
        log.info("mock插件onLoad，end...");
    }

    @Override
    public void onUnload() throws Throwable {
        heartbeatHandler.stop();
    }

    @Override
    public void onActive() throws Throwable {

    }

    @Override
    public void onFrozen() throws Throwable {

    }

    @Override
    public void loadCompleted() {
        log.info("mock插件loadCompleted，start...");
        heartbeatHandler = new HeartbeatHandler(configInfo, moduleManager);
        heartbeatHandler.start();
        updateMockRulesHandler.start();
        initialize();
        log.info("mock插件loadCompleted，end...");
    }


    @Command("checkActive")
    public void active(){
        log.info("收到探活请求");
    }

    /**
     * 插件初始化
     */
    private synchronized void initialize() {
        log.info("插件初始化，start...");
        ServiceLoader<MockPlugin> mockPlugins = ServiceLoader.load(MockPlugin.class);        // 执行不同厂商的业务实现，具体根据业务需求配置
        for (MockPlugin mockPlugin : mockPlugins) {
            //todo 删除
            if( mockPlugin.enable(new MockConfig())){
                log.info("插件"+mockPlugin.identity()+"初始化，start...");
                try {
                    mockPlugin.watch(eventWatcher,invocationListener);
                } catch (PluginLifeCycleException e){
                    e.printStackTrace();
                    log.info("插件"+mockPlugin.identity()+"初始化，end...success");
                }
                log.info("插件"+mockPlugin.identity()+"初始化，end...success");
            }
        }
        log.info("插件初始化，end...");
    }



}
