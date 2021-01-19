package com.shidan.ark.mock.core.enums;

import java.util.Arrays;
import java.util.Optional;

public enum MockPluginEnum {

    // todo,待补充，插件indentity与平台mockType映射

    dubboComsumer("dubbo-consumer", "1"),
    ;

    MockPluginEnum(String identity, String mockTypeCode) {
        this.identity = identity;
        this.mockTypeCode = mockTypeCode;
    }

    private String identity;

    private String mockTypeCode;


    public String getIdentity() {
        return identity;
    }

    public String getMockTypeCode() {
        return mockTypeCode;
    }

    public static String getIdentityByCode(String code) {
        Optional<MockPluginEnum> result = Arrays.stream(MockPluginEnum.values()).filter(item -> item.getMockTypeCode().equals(code)).findFirst();
        return result.get().getIdentity();
    }
}
