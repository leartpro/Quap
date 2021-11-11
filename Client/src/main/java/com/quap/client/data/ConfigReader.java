package com.quap.client.data;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;


public class ConfigReader {
    Preferences prefs;


    public ConfigReader(String username) throws IOException, InvalidPreferencesFormatException {
        prefs = Preferences.userNodeForPackage(Gadget.class);
        Preferences.importPreferences(new FileInputStream("/users/"+username+"/preferences/settings.xml"));
    }

    private class Gadget {
        private static final Theme THEME = Theme.LIGHT;


        private enum Theme {
            DARK, LIGHT
        }
    }
}
