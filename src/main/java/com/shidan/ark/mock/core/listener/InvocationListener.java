package com.shidan.ark.mock.core.listener;

import com.shidan.ark.mock.core.model.Invocation;

public interface InvocationListener {

    /**
     * invocation回调逻辑
     *
     * @param invocation 组装的调用信息
     * @see Invocation
     */
    void onInvocation(final Invocation invocation);
}
