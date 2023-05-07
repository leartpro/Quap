package com.quap.client.data;

import java.io.File;

public record ConfigReader(String username) {

    public void init() {
        String rootPath = "Client/src/main/resources/com/quap/users/" + username;
        File sqlFolder;
        sqlFolder = new File(rootPath + "/sqlite/db/");
        boolean success;
        success = sqlFolder.mkdirs();
        if (!success) {
            System.err.println("Can not create path " + rootPath + "/sqlite/db/");
        }
    }

    public void validateUser() {
        String rootPath = "Client/src/main/resources/com/quap/users/" + username;
        File folder = new File(rootPath);
        if (!folder.exists()) {
            init();
        }
    }
}
