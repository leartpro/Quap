package com.quap.client.utils;

import com.quap.client.domain.Chat;
import com.quap.client.domain.Friend;
import com.quap.client.domain.Message;

import java.util.List;

public interface MainClientObserver {

    void messageEvent(Message message);

    void createChatEvent(Chat chat);

    void inviteEvent(Chat chat, int senderID, String senderName, List<String> participants);

    void joinChatEvent(Chat chat);

    void friendRequestEvent(Friend friend);

    void addFriendEvent(Friend friend);

    void unfriendEvent();

    void serverDisconnectEvent(String content, String header);
}
