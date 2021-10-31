package com.quap.controller.vista.main;

import com.quap.controller.vista.VistaNavigator;
import com.quap.controller.vista.login.SignInController;
import com.quap.controller.vista.login.SignUpController;

public abstract class MainVistaNavigator extends VistaNavigator {

    @Override
    public VistaNavigator getVistaByID(String id) {
        if(id.equals("signUp")) {
            return new SignUpController();
        } else if(id.equals("signIn")) {
            return new SignInController();
        } else {
            IllegalArgumentException e;
            return null;
        }
    }

    public abstract void switchMode(boolean b);

    //methods for main controllers



}
