package com.quap.client.data;

import java.io.IOException;
import java.util.prefs.InvalidPreferencesFormatException;

public class ConfigReader {
    String username;

    public ConfigReader(String username) throws IOException, InvalidPreferencesFormatException {
        this.username = username;
    }
}
