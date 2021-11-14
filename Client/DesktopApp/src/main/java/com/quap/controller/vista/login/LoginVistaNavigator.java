package com.quap.controller.vista.login;

import com.quap.controller.vista.VistaNavigator;

public abstract class LoginVistaNavigator extends VistaNavigator {

    @Override
    protected VistaNavigator getVistaByID(String id) {
        if(id.equals("signUp")) {
            return new SignUpController();
        } else if(id.equals("signIn")) {
            return new SignInController();
        } else {
            IllegalArgumentException e;
            return null;
        }
    }

    public abstract boolean validLogin();

    public abstract void switchMode(boolean isSelected);
}
