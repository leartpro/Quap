package com.quap.controller.vista.main;

import com.quap.client.Client;

public class SettingsController extends MainVistaNavigator{
    private Client client;

    @Override
    public void loadContent(Object... content) {

    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }
}
