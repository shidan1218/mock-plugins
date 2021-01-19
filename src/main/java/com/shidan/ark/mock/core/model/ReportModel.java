package com.shidan.ark.mock.core.model;

/**
 * @author ：shidan
 */
public class ReportModel {

    private String app;

    private String environment;

    private String ip;

    private String version;

    private String tempId;

    private String mockType;

    private String tempSnapshot;

    private String path;

    private String method;

    private String realInput;

    private String realOutput;



    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public String getMockType() {
        return mockType;
    }

    public void setMockType(String mockType) {
        this.mockType = mockType;
    }

    public String getTempSnapshot() {
        return tempSnapshot;
    }

    public void setTempSnapshot(String tempSnapshot) {
        this.tempSnapshot = tempSnapshot;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRealInput() {
        return realInput;
    }

    public void setRealInput(String realInput) {
        this.realInput = realInput;
    }

    public String getRealOutput() {
        return realOutput;
    }

    public void setRealOutput(String realOutput) {
        this.realOutput = realOutput;
    }
}
