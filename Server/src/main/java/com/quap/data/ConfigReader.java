package com.quap.data;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ConfigReader {

    public void loadDefaultProperties() throws IOException {
        FileWriter writer = new FileWriter("Server/src/main/resources/com/quap/config/config.properties");

        OrderedProperties database = new OrderedProperties(new LinkedHashMap<>() ,true);
        database.setProperty("database-postgres-name", "postgres@localhost");
        database.setProperty("database-postgres-socket", "localhost:5432");
        database.setProperty("database-postgres-username", "postgres");
        database.setProperty("database-postgres-password", "password");
        database.store(writer, "DATABASE");

        OrderedProperties runtime = new OrderedProperties(new LinkedHashMap<>() ,true);
        runtime.setProperty("runtime-maxThreads", "null");
        runtime.store(writer, "RUNTIME");

        OrderedProperties network = new OrderedProperties(new LinkedHashMap<>() ,true);
        network.setProperty("network-prefer-protocol", "java.net.preferIPv4Stack");
        network.setProperty("network-adapter-name", "Ethernet");
        network.setProperty("network-ip-address", "192.168.56.1");
        network.setProperty("network-subnet-mask", "255.255.0.0");
        network.setProperty("network-default-gateway", "192.168.178.1");
        network.store(writer, "NETWORK");

        OrderedProperties socket = new OrderedProperties(new LinkedHashMap<>() ,true);
        socket.setProperty("network-socket-hostname", "192.168.178.69");
        socket.setProperty("network-socket-port", "8192");
        socket.setProperty("network-socket-backlog", "0");
        socket.store(writer, "SOCKET");
    }

    public Set<Map.Entry<String, String>> getProperties() {
        OrderedProperties props = new OrderedProperties();
        InputStream propsInput = ClassLoader
                .getSystemResourceAsStream("Server/src/main/resources/com/quap/config/config.properties");
        if (propsInput != null) {
            try {
                props.load(propsInput);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new LinkedHashSet<>(props.entrySet());
    }
}
