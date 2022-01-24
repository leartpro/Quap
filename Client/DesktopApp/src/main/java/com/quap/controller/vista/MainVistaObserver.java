package com.quap.controller.vista;

import com.quap.client.domain.Chat;

public interface MainVistaObserver {
    void deleteChatEvent(Chat chat);
}
