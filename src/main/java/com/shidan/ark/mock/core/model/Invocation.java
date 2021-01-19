package com.shidan.ark.mock.core.model;

import com.shidan.ark.mock.core.plugins.MockType;

/**
 * @author ：shidan
 */
public class Invocation {

    /**
     * 插件identity
     */
    private String identity;

    /**
     * 调用类型
     **/
    private MockType type;

    /**
     * @see com.alibaba.jvm.sandbox.api.event.InvokeEvent#invokeId
     */
    private int invokeId;

    /**
     * @see com.alibaba.jvm.sandbox.api.event.InvokeEvent#processId
     */
    private int processId;

    /**
     * 链路跟踪ID（仅应用内部，对于分布式系统需要自己重新定义）
     */
    private String traceId;

    private String path;

    private String method;

    /**
     * 请求参数 - snapshot 不做传输使用，传输需要序列化值
     *
     * @see Invocation#requestSerialized
     */
    private transient Object[] request;

    /**
     * 序列化之后的请求值，录制时候作为{@link Invocation#request}的载体传输；回放时候需要还原成{@link Invocation#request}
     */
    private String requestSerialized;

    /**
     * 返回结果 - snapshot 不做传输使用
     */
    private transient Object response;

    /**
     * 序列化之后的请求值，录制时候作为{@link Invocation#response}的载体传输；回放时候需要还原成{@link Invocation#response}
     */
    private String responseSerialized;

    /**
     * 异常信息 - snapshot 不做传输使用
     */
    private transient Throwable throwable;

    /**
     * 序列化之后的请求值，录制时候作为{@link Invocation#throwable}的载体传输；回放时候需要还原成{@link Invocation#throwable}
     */
    private String throwableSerialized;

    /**
     * 调用开始时间
     */
    private Long start;

    /**
     * 调用结束时间
     */
    private Long end;


    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public MockType getType() {
        return type;
    }

    public void setType(MockType type) {
        this.type = type;
    }

    public int getInvokeId() {
        return invokeId;
    }

    public void setInvokeId(int invokeId) {
        this.invokeId = invokeId;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
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

    public Object[] getRequest() {
        return request;
    }

    public void setRequest(Object[] request) {
        this.request = request;
    }

    public String getRequestSerialized() {
        return requestSerialized;
    }

    public void setRequestSerialized(String requestSerialized) {
        this.requestSerialized = requestSerialized;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public String getResponseSerialized() {
        return responseSerialized;
    }

    public void setResponseSerialized(String responseSerialized) {
        this.responseSerialized = responseSerialized;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getThrowableSerialized() {
        return throwableSerialized;
    }

    public void setThrowableSerialized(String throwableSerialized) {
        this.throwableSerialized = throwableSerialized;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }
}
