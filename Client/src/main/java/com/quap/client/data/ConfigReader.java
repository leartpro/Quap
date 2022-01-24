package com.quap.client.data;

import java.io.File;

public class ConfigReader {
    private final String username;

    public ConfigReader(String username) {
        this.username = username;
    }

    public void init() {
        String rootPath = "Client/src/main/resources/com/quap/users/" + username;
        File sqlFolder;
        sqlFolder = new File(rootPath + "/sqlite/db/");
        sqlFolder.mkdirs();
    }

    public void validateUser() {
        String rootPath = "Client/src/main/resources/com/quap/users/" + username;
        File folder = new File(rootPath);
        if(!folder.exists()) {
            init();
        }
    }
}
