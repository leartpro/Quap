package com.quap.controller.vista;

public interface LoginVistaObserver extends VistaObserver{
    void swapVistaEvent(String signUp);
    void toggleLoginEvent(boolean b);
}
