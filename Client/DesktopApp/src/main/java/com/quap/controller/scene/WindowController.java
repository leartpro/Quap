package com.quap.controller.scene;

import com.quap.controller.vista.VistaNavigator;
import com.quap.controller.vista.VistaObserver;
import javafx.scene.Parent;

public abstract class WindowController implements VistaObserver {
    public abstract void setVista(Parent node, VistaNavigator controller);
}
