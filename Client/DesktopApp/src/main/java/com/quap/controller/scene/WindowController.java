package com.quap.controller.scene;

import com.quap.controller.vista.VistaNavigator;
import javafx.scene.Parent;

public abstract class WindowController {
    public abstract void setVista(Parent node, VistaNavigator controller);
}
