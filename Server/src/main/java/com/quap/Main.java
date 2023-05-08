package com.quap;

import com.quap.data.ConfigReader;
import com.quap.server.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * Die Klasse stellt die Main-Klasse des Servers dar und enthält die main-Methode
 */
public class Main {
    private static ServerSocket socket;
    /**
     * Die Methode verarbeitet die Argumente, mit denen die Klasse aufgerufen wurde.
     * Wenn die Socket sich zu der ihr zugewiesenen Adresse binden kann, wird die Serverklasse aufgerufen
     * @param args nicht erforderlich
     */
    public static void main(String[] args) {
        //read properties
        ConfigReader configReader = new ConfigReader();
        Set<Map.Entry<String, String>> entrySet = configReader.getProperties();
        HashMap<String, String> properties = new HashMap<>();
        for (Map.Entry<String, String> entry : entrySet) {
            properties.put(entry.getKey(), entry.getValue());
        }
        //set network address scope
        System.setProperty("java.net.preferIPv4Stack", "false");
        System.setProperty("java.net.preferIPv6Stack", "false");
        if(properties.get("network-address-scope").equals("IPv4")) {
            System.setProperty("java.net.preferIPv4Stack", "true");
        } else if(properties.get("network-address-scope").equals("IPv6")) {
            System.setProperty("java.net.preferIPv6Stack", "true");
        } else {
            System.err.println("Unknown network address scope");
        }
        //open socket
        String ip = properties.get("network-socket-hostname");
        int port = Integer.parseInt(properties.get("network-socket-port"));
        int backlog = Integer.parseInt(properties.get("network-socket-backlog"));
        try {
            socket = new ServerSocket();
            socket.bind(new InetSocketAddress(ip, port), backlog);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //initialize executor service
        int maxThreads = Integer.parseInt(properties.get("runtime-maxThreads"));
        ExecutorService service = newFixedThreadPool(maxThreads);
        //database connection
        String name, url, username, password;
        name = properties.get("database-name");
        url = properties.get("database-url");
        username = properties.get("database-username");
        password = properties.get("database-password");
        Connection db_connection;
        try {
            db_connection = DriverManager.getConnection(
                    "jdbc:postgresql://" + url + "/" + name, username, password
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        new Server(socket, service, db_connection);
    }
}