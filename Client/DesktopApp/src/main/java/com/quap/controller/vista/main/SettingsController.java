package com.quap.controller.vista.main;

import com.quap.client.Client;
import com.quap.client.domain.Content;
import com.quap.controller.vista.MainVistaObserver;

import java.util.ArrayList;
import java.util.List;

public class SettingsController extends MainVistaNavigator{
    private Client client;
    private String type;
    private final List<MainVistaObserver> observers = new ArrayList<>();

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

    @Override
    public void addObserver(MainVistaObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(MainVistaObserver observer) {
        observers.remove(observer);
    }
}
