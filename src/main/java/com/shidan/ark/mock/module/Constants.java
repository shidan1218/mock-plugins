package com.shidan.ark.mock.module;

/**
 * @author ：shidan
 */
public class Constants {

    public static final String MODULE_ID = "mock";

    public static final String VERSION = "1.0.0";

    // todo,需要改成控制台地址,后续改成配置文件
    public static final String CONSOLE_ADDRESS = "http://127.0.0.1:7001";

    // todo,需要改成日志路径
    public static final String LOG_PATH = "/home/admin/logs/sandbox/mock";

    public static final String LOG_FILE_NAME=LOG_PATH+"/mock-%u-%g.log";

    public static final String HEARTBEAT_PATH = "/mockModule/alive";

    public static final String MOCK_RULES_PATH = "/mockRule/query";

    public static final String REPORT_PATH = "/mockLog/sync";

}
