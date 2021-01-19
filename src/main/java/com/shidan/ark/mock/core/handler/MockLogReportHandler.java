package com.shidan.ark.mock.core.handler;

import com.alibaba.fastjson.JSON;
import com.shidan.ark.mock.core.model.Invocation;
import com.shidan.ark.mock.core.model.ReportModel;
import com.shidan.ark.mock.module.Constants;
import com.shidan.ark.mock.util.HttpUtil;
import com.shidan.ark.mock.util.LogUtil;

import java.util.logging.Logger;

/**
 * @author ：shidan
 */
public class MockLogReportHandler implements QueueTaskHandler {

    private final static String REPORT_DOMAIN = Constants.CONSOLE_ADDRESS + Constants.REPORT_PATH;

//    private Invocation invocation;

    private ReportModel reportModel;

    public MockLogReportHandler(ReportModel reportModel) {
        this.reportModel = reportModel;
    }


    Logger log = LogUtil.getLogger(MockLogReportHandler.class);


    @Override
    public void processData() {
        log.info("上报mock数据:"+JSON.toJSONString(reportModel));
        HttpUtil.doPostJson(REPORT_DOMAIN, JSON.toJSONString(reportModel));
    }
}
