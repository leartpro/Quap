package com.quap.client;

import com.quap.client.network.Prefixes;
import com.quap.client.network.Suffixes;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Client {
    private static final HashMap<Prefixes, String> prefixes = new HashMap();
    private static final HashMap<Suffixes, String> suffixes = new HashMap();
    private Socket socket;
    private static final int remotePort;
    private static String name;
    public static Connection connection;

    static {
        try {
            name = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        for(Prefixes p : Prefixes.values()) {
            prefixes.put(p, "/+"+ p.name().toLowerCase().charAt(0)+ "+/");
        }

        for(Suffixes s : Suffixes.values()) {
            suffixes.put(s, "/-"+ s.name().toLowerCase().charAt(0)+ "-/");
        }
        remotePort = 8080;
        connection = new Connection();
    }

    public Client(InetAddress address, int port) {
        try {
            //socket = new Socket(InetAddress.getByName("de.quap.com"), 8192, address, port); //TODO: change socket
            socket = new Socket(InetAddress.getByName("de.quap.com"), 8192); //TODO: add local configurations
            //socket.bind(new InetSocketAddress(address, port));
        } catch (IOException e) {
            e.printStackTrace();
        }
        openConnection();
    }

    public void setName(String name) {
        Client.name = name;
    }

    public void send() {

    }

    public void receive() {

    }

    public void openConnection() {

    }

    public void authorize() {
    }


    public static class Connection {
        public boolean isConnected() {
            return true;
        }

        public String getConnectionAddress() {
            return "";
        }

        @Override
        public String toString() {
            return "";
        }
    }
}
