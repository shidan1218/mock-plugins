package com.shidan.ark.mock.core.plugins;

import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.shidan.ark.mock.core.exception.PluginLifeCycleException;
import com.shidan.ark.mock.core.listener.InvocationListener;
import com.shidan.ark.mock.core.model.MockConfig;

public interface MockPlugin {


    /**
     * 调用类型
     *
     * @return 调用类型
     */
    MockType getType();

    /**
     * 身份标识 - 唯一标识一个插件
     *
     * @return identity 能够唯一标志插件
     */
    String identity();

    /**
     * 是否生效
     *
     * @param config 配置文件
     * @return true/false
     */
    boolean enable(MockConfig config);

    /**
     * 被加载之前
     *
     * @throws PluginLifeCycleException 插件周期异常
     */
    void onLoaded() throws PluginLifeCycleException;

    /**
     * 被激活
     *
     * @throws PluginLifeCycleException 插件周期异常
     */
    void onActive() throws PluginLifeCycleException;

    /**
     * 重新初始化 (例如:推送配置之后，需要重新增强代码)
     *
     * @param watcher  增强器
     * @param listener invocation的监听者
     * @throws PluginLifeCycleException 插件周期异常
     */
    void watch(ModuleEventWatcher watcher,
               InvocationListener listener) throws PluginLifeCycleException;

    /**
     * 删除插件
     *
     * @param watcher  增强器
     * @param listener invocation的监听者
     */
    void unWatch(ModuleEventWatcher watcher,
                 InvocationListener listener);

    /**
     * 重新初始化 (例如:推送配置之后，需要重新增强代码)
     *
     * @param watcher  增强器
     * @param listener invocation的监听者
     * @throws PluginLifeCycleException 插件周期异常
     */
    void reWatch(ModuleEventWatcher watcher,
                 InvocationListener listener) throws PluginLifeCycleException;

    /**
     * 被冻结
     *
     * @throws PluginLifeCycleException 插件周期异常
     */
    void onFrozen() throws PluginLifeCycleException;

    /**
     * 被卸载
     *
     * @throws PluginLifeCycleException 插件周期异常
     */
    void onUnloaded() throws PluginLifeCycleException;

    /**
     * 监听配置变化
     *
     * @param config 配置文件
     * @throws PluginLifeCycleException 插件周期异常
     */
    void onConfigChange(MockConfig config) throws PluginLifeCycleException;
}
