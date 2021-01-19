package com.shidan.ark.mock.core.plugins;

import com.alibaba.jvm.sandbox.api.listener.EventListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.google.common.collect.Lists;
import com.shidan.ark.mock.core.exception.PluginLifeCycleException;
import com.shidan.ark.mock.core.listener.InvocationListener;
import com.shidan.ark.mock.core.model.EnhanceModel;
import com.shidan.ark.mock.core.model.MockConfig;
import com.shidan.ark.mock.util.LogUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ：shidan
 */
public abstract class AbstractMockPluginAdapter implements MockPlugin {

//    protected final static Logger log = LoggerFactory.getLogger(AbstractMockPluginAdapter.class);

    private final static Logger log = LogUtil.getLogger(AbstractMockPluginAdapter.class);

    protected volatile MockConfig configTemporary;

    private ModuleEventWatcher watcher;

    private List<Integer> watchIds = Lists.newCopyOnWriteArrayList();

    private InvocationListener listener;

    private AtomicBoolean watched = new AtomicBoolean(false);


    @Override
    public void onLoaded() throws PluginLifeCycleException {
        // default no-op
    }

    @Override
    public void onActive() throws PluginLifeCycleException {
        // default no-op
    }

    @Override
    public void watch(ModuleEventWatcher watcher, InvocationListener listener) throws PluginLifeCycleException {
        log.info("watch start...");
        this.watcher = watcher;
        this.listener = listener;
        watchIfNecessary();
        log.info("watch end...success");
    }

    @Override
    public void unWatch(ModuleEventWatcher watcher, InvocationListener listener) {
        if (CollectionUtils.isNotEmpty(watchIds)) {
            for (Integer watchId : watchIds) {
                watcher.delete(watchId);
            }
            watchIds.clear();
        }
        watched.compareAndSet(true, false);
    }

    @Override
    public void reWatch(ModuleEventWatcher watcher, InvocationListener listener) throws PluginLifeCycleException {
        this.unWatch(watcher, listener);
        watch(watcher, listener);
    }

    @Override
    public void onFrozen() throws PluginLifeCycleException {
        // default no-op
    }

    @Override
    public void onUnloaded() throws PluginLifeCycleException {
        // default no-op
    }

    @Override
    public void onConfigChange(MockConfig config) throws PluginLifeCycleException {
        // default no-op;plugin can override this method to aware config change
        this.configTemporary = config;
    }

    @Override
    public boolean enable(MockConfig config) {
        Boolean result = config.getPluginIdentitiesEnableMap().get(identity()) == null;
        return config != null && (result == null ? true : result);
    }

    protected void reWatch0() throws PluginLifeCycleException {
        reWatch(watcher, listener);
    }

    /**
     * 执行观察事件
     *
     * @throws PluginLifeCycleException 插件异常
     */
    private synchronized void watchIfNecessary() throws PluginLifeCycleException {
        log.info(identity() + "执行观察事件...start");
        if (watched.compareAndSet(false, true)) {
            List<EnhanceModel> enhanceModels = getEnhanceModels();
            if (CollectionUtils.isEmpty(enhanceModels)) {
                throw new PluginLifeCycleException("enhance models is empty, plugin type is " + identity());
            }
            for (EnhanceModel em : enhanceModels) {
                EventWatchBuilder.IBuildingForBehavior behavior = null;
                EventWatchBuilder.IBuildingForClass builder4Class = new EventWatchBuilder(watcher).onClass(em.getClassPattern());
                log.info(identity() + "增强类" + em.getClassPattern());
                if (em.isIncludeSubClasses()) {
                    builder4Class = builder4Class.includeSubClasses();
                }
                for (EnhanceModel.MethodPattern mp : em.getMethodPatterns()) {
                    log.info(identity() + "增强方法" + mp.getMethodName());
                    behavior = builder4Class.onBehavior(mp.getMethodName());
                    if (ArrayUtils.isNotEmpty(mp.getParameterType())) {
                        behavior.withParameterTypes(mp.getParameterType());
                    }
                    if (ArrayUtils.isNotEmpty(mp.getAnnotationTypes())) {
                        behavior.hasAnnotationTypes(mp.getAnnotationTypes());
                    }
                }
                if (behavior != null) {
                    int watchId = behavior.onWatch(getEventListener(listener), em.getWatchTypes()).getWatchId();
                    watchIds.add(watchId);
                    log.info(identity() + "add watcher success,type=" + getType().name() + ",watcherId=" + watchId);
                } else {
                    log.info(identity() + "found 0 class & method");
                }
            }
        }
        log.info(identity() + "执行观察事件...end");
    }

    /**
     * 获取需要增强的类模型
     *
     * @return enhanceModels
     */
    abstract protected List<EnhanceModel> getEnhanceModels();

//    /**
//     * 返回调用过程处理器，用于处理入参、返回值等
//     *
//     * @return invocationProcessor构造结果
//     */
//    abstract protected InvocationProcessor getInvocationProcessor();

    /**
     * 返回事件监听器 - 子类若参数的组装方式不适配，可以重写改方法
     *
     * @param listener 调用监听
     * @return 事件监听器
     */
//    protected EventListener getEventListener(InvocationListener listener) {
//        return new DefaultEventListener(getType(), isEntrance(), listener, getInvocationProcessor());
//    }
    abstract protected EventListener getEventListener(InvocationListener listener);
}
