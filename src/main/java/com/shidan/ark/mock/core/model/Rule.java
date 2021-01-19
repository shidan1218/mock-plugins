package com.shidan.ark.mock.core.model;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ：shidan
 */
public class Rule implements Comparable<Rule>, Serializable {


    private String tempId;

    private String tempName;

    private String mockType;

    private String path;

    private String method;

    private String matchType;

    private String matchRule;

    private String outputTemp;

    private LocalDateTime modifyTime;

    private String status;


    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public String getMockType() {
        return mockType;
    }

    public void setMockType(String mockType) {
        this.mockType = mockType;
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

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getMatchRule() {
        return matchRule;
    }

    public void setMatchRule(String matchRule) {
        this.matchRule = matchRule;
    }

    public String getOutputTemp() {
        return outputTemp;
    }

    public void setOutputTemp(String outputTemp) {
        this.outputTemp = outputTemp;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int compareTo(Rule o) {
        if (this.modifyTime.isAfter(o.modifyTime)) {
            return 1;
        } else if (this.modifyTime.isBefore(o.modifyTime)) {
            return -1;
        } else
            return 0;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
