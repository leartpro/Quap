package com.quap.runtime;

public interface FutureCallback<V> {
    void onSuccess(V result);

    void onFailure(Throwable failure);
}
