package com.quap.client.utils;

import com.quap.client.domain.Chat;
import com.quap.client.domain.Message;

public interface ClientObserver {
    void messageEvent(Message message);
    void createChatEvent(Chat chat);
}
