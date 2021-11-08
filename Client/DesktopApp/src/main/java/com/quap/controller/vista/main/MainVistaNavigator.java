package com.quap.controller.vista.main;

import com.quap.controller.vista.VistaNavigator;

import java.util.List;

public abstract class MainVistaNavigator<V> extends VistaNavigator {

    @Override
    public VistaNavigator getVistaByID(String id) {
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

    public abstract void loadContent(List<V> content);

    //methods for main controllers



}
