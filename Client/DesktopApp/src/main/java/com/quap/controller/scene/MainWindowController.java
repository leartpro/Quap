package com.quap.controller.scene;

import com.quap.controller.vista.VistaNavigator;
import com.quap.controller.vista.login.LoginVistaNavigator;
import com.quap.controller.vista.main.MainVistaNavigator;
import javafx.fxml.FXML;

public class MainWindowController {
    private MainVistaNavigator currentNode;
    private final Vista vista = new Vista();

    private class Vista extends LoginVistaNavigator {

        @Override
        public boolean validLogin() {
            return false;
        }

        @Override
        public void switchMode(boolean isSelected) {

        }

        public VistaNavigator getVistaByID(String ID) {
            return super.getVistaByID(ID);
        }
    }

    @FXML
    public void initialize() {
        currentNode = (MainVistaNavigator) vista.getVistaByID("profile");
    }
    }
