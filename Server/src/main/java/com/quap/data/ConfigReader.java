package com.quap.data;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ConfigReader {
    private final OrderedProperties props;

    public ConfigReader() {
        props = new OrderedProperties();
        //props.load(new FileInputStream(new File("~/some.properties")));

        InputStream propsInput = ClassLoader
                .getSystemResourceAsStream("Server/src/main/resources/com/quap/config/config.properties");
        if (propsInput != null) {
            try {
                props.load(propsInput);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            props.store(new FileWriter("Server/src/main/resources/com/quap/config/config.properties"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDefaultProperties(){
        props.setProperty("database-postgres-name", "postgres@localhost");
        props.setProperty("database-postgres-socket", "localhost:5432");
        props.setProperty("database-postgres-username", "postgres");
        props.setProperty("database-postgres-password", "password");

        props.setProperty("database-cachedDB", "null");
        props.setProperty("database-cachedDB-name", "null");
        props.setProperty("database-cachedDB-socket", "null");
        props.setProperty("database-cachedDB-username", "null");
        props.setProperty("database-cachedDB-password", "null");

        props.setProperty("runtime-allowThreading", "true");
        props.setProperty("runtime-allowCachedThreading", "true");
        props.setProperty("runtime-maxThreads", "null");

        //TODO: configure final network-adapter-settings
        props.setProperty("network-prefer-protocol", "java.net.preferIPv4Stack");
        props.setProperty("network-adapter-name", "Ethernet");
        props.setProperty("network-ip-address", "192.168.56.1");
        props.setProperty("network-subnet-mask", "255.255.0.0");
        props.setProperty("network-default-gateway", "192.168.178.1");

        props.setProperty("network-socket-hostname", "192.168.178.69");
        props.setProperty("network-socket-port", "8192");
        props.setProperty("network-socket-backlog", "0");
    }

    public HashMap<String, String> getProperties() {
        HashMap<String, String> properties = new HashMap<>((Map<? extends String, ? extends String>) props.entrySet());
        return properties;
    }
}
