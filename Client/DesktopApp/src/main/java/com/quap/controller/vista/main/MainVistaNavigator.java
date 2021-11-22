package com.quap.controller.vista.main;

import com.quap.client.Client;
import com.quap.controller.vista.VistaNavigator;

public abstract class MainVistaNavigator extends VistaNavigator {

    @Override
     protected VistaNavigator getVistaByID(String id) {
        //TODO:
        if(id.equals("list")) {
            return new ListController();
        } else if(id.equals("profile")) {
            return new ProfilController();
        } else if(id.equals("chat")) {
            return new ChatController();
        } else if(id.equals("settings")) {
            return new SettingsController();
        } else {
            IllegalArgumentException e;
            return null;
        }
    }

    public abstract <T> void loadContent(T... content);

    public abstract void setClient(Client client);

    //methods for main controllers



}
