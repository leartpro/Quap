package com.quap.client.data;

import java.io.*;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class ConfigReader {
    private final Preferences prefs;
    private final Properties props;
    private final String username;

    public ConfigReader(String username) {
        this.username = username;
        prefs = Preferences.userRoot().node("com/quap/users/" + username + "/preferences/settings");
        props = new Properties();
    }

    public void createUser() {
        init(username);
        initLocalProperties(username);
        initLocalPreferences(username);
    }

    public void deleteUser() {
        delete(username);
        deleteLocalProperties(username);
        deleteLocalPreferences(username);
    }

    public void readUser() {
        read(username);
        readLocalProperties(username);
        readLocalPreference(username);

    }

    private void initLocalPreferences(String username) {

        //set default values here
        prefs.putInt("bind_port", 80);
        prefs.put("bind_address", "localhost");

        //request values here
        int bind_port = prefs.getInt("bind_port", 80);
        String bind_address = prefs.get("bind_address", "localhost");
        System.out.println("BIND_PORT:" + bind_port);
        System.out.println("BIND_ADDRESS:" + bind_address);

        try {
            prefs.exportNode(
                    new FileOutputStream(
                            "Client/src/main/resources/com/quap/users/"
                                    + username
                                    + "/preferences/config/config.xml")
            );
        } catch (IOException | BackingStoreException e) {
            e.printStackTrace();
        }
    }

    private void initLocalProperties(String username) {
        InputStream propsInput = ClassLoader
                .getSystemResourceAsStream(
                        "Client/src/main/resources/com/quap/users/"
                        + username
                        + "/preferences/settings/settings.properties");
        if (propsInput != null) {
            try {
                props.load(propsInput);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //set default values here
        props.setProperty("theme", "light");

        try {
            props.store(new FileWriter("Client/src/main/resources/com/quap/users/" + username + "/preferences/settings/settings.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(String username) {
        //TODO: mkdir() might not the best and most modern solution
        String rootPath = "Client/src/main/resources/com/quap/users/" + username;
        File sqlFolder, prefFolder;
        File prefFile;
        sqlFolder = new File(rootPath + "/sqlite/db/");
        sqlFolder.mkdirs();
        prefFolder = new File(rootPath + "/preferences/settings/");
        prefFolder.mkdirs();
        prefFile = new File(rootPath + "/preferences/settings/" + "settings.properties");
        prefFolder = new File(rootPath + "/preferences/config/");
        prefFolder.mkdirs();
        try {
            prefFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteLocalPreferences(String username) {
    }

    private void deleteLocalProperties(String username) {
    }

    private void delete(String username) {
    }

    private void readLocalPreference(String username) {
    }

    private void readLocalProperties(String username) {
    }

    private void read(String username) {
    }

    //TODO: add listener for all main config-vars

}
