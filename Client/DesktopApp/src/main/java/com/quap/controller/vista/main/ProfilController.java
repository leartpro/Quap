package com.quap.controller.vista.main;

import com.quap.client.Client;
import com.quap.client.domain.Content;

import java.util.List;

public class ProfilController extends MainVistaNavigator{
    private Client client;
    private String type;

    @Override
    public void loadContent(List<Content> content) {

    }

    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void setType(String id) {
        this.type = id;
    }

    @Override
    public String getType() {
        return type;
    }
}
