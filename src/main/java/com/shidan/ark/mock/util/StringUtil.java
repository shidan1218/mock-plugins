package com.shidan.ark.mock.util;

/**
 * @author ：shidan
 */
public class StringUtil {

    static String merge(Object... strs){
        String result="";
        for(Object str:strs){
            result=result+"|"+strs.toString();
        }
        return result;
    }

    /**
     * 快捷工具，模仿 slf4j 占位符输出
     */
    public static String truncStr(Object... strs) {
        if (strs == null) return null;
        if (strs.length < 2) return merge(strs);
        String str0 = strs[0].toString();
        if (!str0.contains("{}")) {
            return merge(strs);
        }
        str0 = str0.replaceAll("\\{\\}", "%s");
        Object[] objs = new Object[strs.length - 1];
        int len = strs.length;
        for (int i = 1; i < len; i++) {
            objs[i - 1] = strs[i];
        }
        return String.format(str0, objs);
    }

}
