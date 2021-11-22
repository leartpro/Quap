package com.quap.controller.vista.main;

import com.quap.client.Client;
import com.quap.client.domain.UserContent;

import java.util.List;

public class SettingsController extends MainVistaNavigator{
    private Client client;

    @Override
    public void loadContent(List<UserContent> content) {

    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }
}
