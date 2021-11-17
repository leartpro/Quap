package com.quap.data;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private final Properties props;

    public ConfigReader() {
        props = new Properties();
        //TODO: database pw and username...

        InputStream propsInput = ClassLoader
                .getSystemResourceAsStream("Server/src/main/resources/com/quap/config/config.properties");
        if (propsInput != null) {
            try {
                props.load(propsInput);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        props.setProperty("theme", "light"); //TODO: sql settings, threading settings, network settings

        try {
            props.store(new FileWriter("Server/src/main/resources/com/quap/config/config.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
