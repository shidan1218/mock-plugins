package com.shidan.ark.mock.core.enums;

public enum MatchTypeEnum {

    Regex("1", "regex"),
    ;

    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    MatchTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
