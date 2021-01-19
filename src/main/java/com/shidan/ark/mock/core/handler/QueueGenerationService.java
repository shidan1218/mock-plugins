package com.shidan.ark.mock.core.handler;

import com.shidan.ark.mock.util.LogUtil;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * @author ：shidan
 */
public class QueueGenerationService {

    static Logger log = LogUtil.getLogger(QueueGenerationService.class);

    private static final LinkedBlockingQueue<QueueTaskHandler> tasks = new LinkedBlockingQueue<QueueTaskHandler>(50000);

    private static ExecutorService service = Executors.newSingleThreadExecutor();

    //检查服务是否运行
    private static volatile boolean running = true;

    //线程状态
    private static Future<?> serviceThreadStatus = null;

    static {
        init();
    }


    public static void init() {
        log.info("QueueGenerationService init start");
        serviceThreadStatus = service.submit(new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    try {
                        //开始一个任务
                        QueueTaskHandler task = tasks.take();
                        task.processData();
                    } catch (InterruptedException e) {
                        log.info("mock数据异步上报异常，停止上报" + e.getLocalizedMessage());
                        running = false;
                    }
                }
            }
        }, "mock data report thread"));
        log.info("QueueGenerationService init end");
    }

    public static boolean addData(QueueTaskHandler dataHandler) {
        if (!running) {
            log.info("当前状态为停止上报，加入队列失败");
            return false;
        }
        boolean success = tasks.offer(dataHandler);
        if (!success) {
            log.info("加入队列失败，队列已满");
        } else log.info("加入队列成功");
        return success;
    }

    public static boolean isEmpty() {
        return tasks.isEmpty();
    }

    public static boolean checkRun() {
        return running && !service.isShutdown() && !serviceThreadStatus.isDone();
    }

    public void active() {
        running = true;
        if (service.isShutdown()) {
            service = Executors.newSingleThreadExecutor();
            init();
            log.info("mock数据上报线程池关闭，重新初始化线程池及任务");
        }
        if (serviceThreadStatus.isDone()) {
            init();
            log.info("线程池任务结束，重新初始化任务");
        }
    }

    @PreDestroy
    public void destory() {
        running = false;
        service.shutdownNow();
    }


}
