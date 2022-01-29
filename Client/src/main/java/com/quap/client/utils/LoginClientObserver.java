package com.quap.client.utils;

public interface LoginClientObserver {
    void authFailedEvent(String error);

    void authSuccessEvent();
}
