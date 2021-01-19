package com.shidan.ark.mock.core.handler;

import com.alibaba.jvm.sandbox.api.ModuleException;
import com.alibaba.jvm.sandbox.api.resource.ConfigInfo;
import com.alibaba.jvm.sandbox.api.resource.ModuleManager;
import com.shidan.ark.mock.core.model.ApplicationModel;
import com.shidan.ark.mock.module.Constants;
import com.shidan.ark.mock.util.HttpUtil;
import com.shidan.ark.mock.util.LogUtil;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * @author ：shidan
 */
public class HeartbeatHandler {

    Logger log = LogUtil.getLogger(HeartbeatHandler.class);

    private static final long FREQUENCY = 10;

    // todo,心跳上报地址从属性文件获取
    private final static String HEARTBEAT_DOMAIN = Constants.CONSOLE_ADDRESS + Constants.HEARTBEAT_PATH;


    private static ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("heartbeat-pool-%d").daemon(true).build());

    private final ConfigInfo configInfo;
    private final ModuleManager moduleManager;
    private AtomicBoolean initialize = new AtomicBoolean(false);

    public HeartbeatHandler(ConfigInfo configInfo, ModuleManager moduleManager) {
        this.configInfo = configInfo;
        this.moduleManager = moduleManager;
    }

    public synchronized void start() {
        log.info("心跳上报定时任务(10s间隔)，启动");
        if (initialize.compareAndSet(false, true)) {
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        innerReport();
                    } catch (Exception e) {
                        log.info("心跳定时任务启动异常，" + e.getMessage());
                    }
                }
            }, 0, FREQUENCY, TimeUnit.SECONDS);
        }
    }

    public void stop() {
        log.info("心跳上报定时任务，停止");
        if (initialize.compareAndSet(true, false)) {
            executorService.shutdown();
        }
    }

    private void innerReport() {
        Map<String, String> params = new HashMap<String, String>(8);
        params.put("appName", ApplicationModel.instance().getAppName());
        params.put("ip", ApplicationModel.instance().getHost());
        params.put("environment", ApplicationModel.instance().getEnvironment());
        params.put("port", configInfo.getServerAddress().getPort() + "");
        params.put("version", Constants.VERSION);
        try {
            params.put("status", moduleManager.isActivated(Constants.MODULE_ID) ? "active" : "frozen");
        } catch (ModuleException e) {
            log.info("心跳上报异常，"+e.getMessage());
        }
        HttpUtil.doPostJson(HEARTBEAT_DOMAIN, params);
    }

}
