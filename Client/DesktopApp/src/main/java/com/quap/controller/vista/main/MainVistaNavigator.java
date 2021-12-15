package com.quap.controller.vista.main;

import com.quap.client.Client;
import com.quap.client.domain.UserContent;
import com.quap.controller.vista.VistaNavigator;

import java.util.List;

public abstract class MainVistaNavigator extends VistaNavigator {

    @Override
     protected VistaNavigator getVistaByID(String id) {
        //TODO:
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
                IllegalArgumentException e;
                return null;
        }
    }

    public abstract void loadContent(List<UserContent> content);

    public abstract void setClient(Client client);

    public abstract void setType(String id);

    public abstract String getType();

    //methods for main controllers



}
