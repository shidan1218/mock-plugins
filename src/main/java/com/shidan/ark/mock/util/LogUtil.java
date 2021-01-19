package com.shidan.ark.mock.util;

import com.shidan.ark.mock.module.Constants;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.*;

/**
 * @author ：shidan
 */
public class LogUtil {

    static String demoFile = Constants.LOG_PATH;

    static String logFileName = Constants.LOG_FILE_NAME;

    static Map<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();

    static FileHandler fileHandler = null;


    public static String transferLongToDate(Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date(millSec);
        return sdf.format(date);
    }


    private static FileHandler getFileHandler() {
        if (fileHandler != null) return fileHandler;
        System.err.println("~~~> 生成 fileHandler");
        try {
            //创建文件路径，不然会io异常
            File dir = new File(demoFile);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            //生成1G大小的日志文件，最多100个，开启日志追加记录
            FileHandler f = new FileHandler(logFileName, 1024 * 1024 * 1024, 100, true);
            f.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    String defaultStr = "\n[{}][{}]{}: {}";
                    if (this.getClass().getName().equals(record.getLoggerName())) {
                        defaultStr = "\n[{}][{}]{} {}";
                    }
                    return record.getLevel() + " " + transferLongToDate(record.getMillis()) + " " + record.getLoggerName() + "  " + record.getMessage() + "\n";
                }
            });
            //f.setLevel(Level.FINE); //默认 all
            f.setEncoding("UTF-8");
            //f.publish();
            fileHandler = f;
            return f;
        } catch (IOException e) {
            e.printStackTrace();
            //throw new MessageException("Create FileHandler fail.");
        }
        return null;
    }

    public static Logger getLogger(Class<?> cls) {
        String name = cls.getName();
        Logger logger;
        if (loggerMap.containsKey(name)) {
            logger = loggerMap.get(name);
        } else {
            logger = Logger.getLogger(name);
            //logger.addHandler(getConsoleHandler());
            logger.addHandler(getFileHandler());
            loggerMap.put(name, logger);
        }
        logger.setLevel(Level.ALL);
        return logger;
    }


    public static void error(Throwable throwable, Object... msgs) {
        Logger logger = getLogger(LogUtil.class);
        logger.log(Level.SEVERE, StringUtil.truncStr(msgs), throwable);
    }

    public static void error(Throwable throwable) {
        Logger logger = getLogger(LogUtil.class);
        logger.log(Level.SEVERE, excetpionMsg(throwable).toString(), throwable);
    }

    public static void info(Object... msgs) {
        Logger logger = getLogger(LogUtil.class);
        logger.log(Level.INFO, StringUtil.truncStr(msgs));
    }

    public static StringBuilder excetpionMsg(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stes = throwable.getStackTrace();
        for (StackTraceElement ste : stes) {
            //System.out.println(ste.toString());
            sb.append("\r\n");
            sb.append("~~> ").append(ste.getFileName());
            sb.append("[" + ste.getLineNumber() + "]").append("\r\n");
            sb.append(ste.getClassName()).append(".").append(ste.getMethodName());
            sb.append(": ").append(throwable.getMessage());
        }
        return sb;
    }


}
