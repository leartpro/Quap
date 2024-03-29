package com.quap.controller.vista.main;

import com.quap.client.Client;
import com.quap.client.domain.Content;
import com.quap.controller.vista.MainVistaObserver;
import com.quap.controller.vista.VistaNavigator;

import java.util.List;

public abstract class MainVistaNavigator extends VistaNavigator {

    public abstract void loadContent(List<Content> content);

    public abstract void setClient(Client client);

    public abstract void setType(String id);

    public abstract String getType();

    public abstract void addObserver(MainVistaObserver observer);

    public abstract void removeObserver(MainVistaObserver observer);



}
