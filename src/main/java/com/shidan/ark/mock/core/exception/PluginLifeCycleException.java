package com.shidan.ark.mock.core.exception;

/**
 * @author ：shidan
 * @version: 1.0
 */
public class PluginLifeCycleException extends Exception {

    public PluginLifeCycleException(String message) {
        super(message);
    }

    public PluginLifeCycleException(String message, Throwable cause) {
        super(message, cause);
    }
}
