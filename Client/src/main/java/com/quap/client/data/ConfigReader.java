package com.quap.client.data;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;


public class ConfigReader {
    static Preferences prefs;


    public ConfigReader(String username) throws IOException, InvalidPreferencesFormatException {
        Preferences.importPreferences(new FileInputStream("/users/"+username+"/preferences/settings.xml"));
    }

    public String getTheme() {
        return prefs.get("theme", "light");
    }

    public Config readConfiguration() {
        return new Config();
    }
}
