package com.quap.client.data;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.prefs.InvalidPreferencesFormatException;

public class ConfigReader implements Callable {
    String username;

    public ConfigReader(String username) throws IOException, InvalidPreferencesFormatException {
        this.username = username;
    }

    @Override
    public Object call() throws Exception {
        return null;
    }
}
