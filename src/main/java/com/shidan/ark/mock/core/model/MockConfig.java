package com.shidan.ark.mock.core.model;

import java.util.HashMap;

/**
 * @author ：shidan
 */
public class MockConfig {




    /**
     * 模块启用
     */
    private boolean enable=true;

    /**
     * 插件启动状态
     */
    private static HashMap<String,Boolean>  pluginIdentitiesEnableMap =new HashMap<String, Boolean>();



    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public HashMap<String, Boolean> getPluginIdentitiesEnableMap() {
        return pluginIdentitiesEnableMap;
    }

    public void setPluginIdentitiesEnableMap(HashMap<String, Boolean> pluginIdentitiesEnableMap) {
        this.pluginIdentitiesEnableMap = pluginIdentitiesEnableMap;
    }



    public static void main(String[] args){
        new MockConfig().getPluginIdentitiesEnableMap().get("1");

    }

}
