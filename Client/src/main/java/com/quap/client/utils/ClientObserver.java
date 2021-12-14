package com.quap.client.utils;

import com.quap.client.domain.Chat;
import com.quap.client.domain.Message;

import java.util.List;

public interface ClientObserver {
    void messageEvent(Message message);
    void createChatEvent(Chat chat);
    void inviteEvent(Chat chat, int senderID, String senderName, List<String> participants);
    void joinChatEvent(Chat chat);
}
