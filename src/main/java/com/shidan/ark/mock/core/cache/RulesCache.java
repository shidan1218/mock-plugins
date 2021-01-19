package com.shidan.ark.mock.core.cache;

import com.shidan.ark.mock.core.model.Rule;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：shidan
 */
public class RulesCache {

    //按插件存放规则，key为identity
    private ConcurrentHashMap<String, List<Rule>> rules = new ConcurrentHashMap<String, List<Rule>>();


    private RulesCache() {

    }

    private static class RulesCacheHolder {
        private static RulesCache instance = new RulesCache();
    }

    public static RulesCache getInstance() {
        return RulesCacheHolder.instance;
    }

    public static List get(String identity) {
        return RulesCacheHolder.instance.rules.get(identity);
    }

    public static void put(String indentity, List<Rule> rules) {
        RulesCacheHolder.instance.rules.put(indentity, rules);
    }

    public static void clear() {
        RulesCacheHolder.instance.rules.clear();
    }

    public static void remove(String identity) {
        RulesCacheHolder.instance.rules.remove(identity);
    }


}
