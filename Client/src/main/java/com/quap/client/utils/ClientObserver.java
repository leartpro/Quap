package com.quap.client.utils;

import com.quap.client.domain.Message;

public interface ClientObserver {
    void messageEvent(Message message);
}
