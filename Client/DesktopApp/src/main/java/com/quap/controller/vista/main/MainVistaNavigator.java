package com.quap.controller.vista.main;

import com.quap.client.Client;
import com.quap.client.domain.Content;
import com.quap.controller.vista.MainVistaObserver;
import com.quap.controller.vista.VistaNavigator;

import java.util.List;

public abstract class MainVistaNavigator extends VistaNavigator {

    @Override
     protected VistaNavigator getVistaByID(String id) {
        switch (id) {
            case "list":
                return new ListController();
            case "profile":
                return new ProfilController();
            case "chat":
                return new ChatController();
            case "settings":
                return new SettingsController();
            default:
                throw new IllegalArgumentException();
        }
    }

    public abstract void loadContent(List<Content> content);

    public abstract void setClient(Client client);

    public abstract void setType(String id);

    public abstract String getType();

    public abstract void addObserver(MainVistaObserver observer);

    public abstract void removeObserver(MainVistaObserver observer);



}
