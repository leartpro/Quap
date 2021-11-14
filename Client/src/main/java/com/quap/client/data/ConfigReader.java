package com.quap.client.data;

import java.io.*;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class ConfigReader {
    private final String username;
    private final Preferences prefs;
    private final Properties props;

    public ConfigReader(String username) {
        this.username = username;
        prefs = Preferences.userRoot().node("com/quap/users/" + username + "/preferences/settings");
        props = new Properties();

         InputStream propsInput = ClassLoader
                 .getSystemResourceAsStream("Client/src/main/resources/com/quap/users/"
                         + username + "/preferences/settings/settings.properties");
            if (propsInput != null) {
                try {
                    props.load(propsInput);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            props.setProperty("theme", "light");

        prefs.putInt("bind_port", 80);
        prefs.put("bind_address", "localhost");
        int bind_port = prefs.getInt("bind_port", 80);
        String bind_address = prefs.get("bind_address", "localhost");

        System.out.println("BIND_PORT:" + bind_port);
        System.out.println("BIND_ADDRESS:" + bind_address);

        OutputStream prefsOutput = null;
        try {
            prefsOutput = new FileOutputStream("Client/src/main/resources/com/quap/users/" + username + "/preferences/config/config.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            prefs.exportNode(prefsOutput);
        } catch (IOException | BackingStoreException e) {
            e.printStackTrace();
        }

        try {
            props.store(new FileWriter("Client/src/main/resources/com/quap/users/" + username + "/preferences/settings/settings.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void settings() {

    }
    //TODO: add listener for all main config-vars

}
