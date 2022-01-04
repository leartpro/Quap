package com.quap.controller.vista;

import com.quap.client.domain.Chat;

public interface MainVistaObserver extends VistaObserver {
    void deleteChatEvent(Chat chat);
}
