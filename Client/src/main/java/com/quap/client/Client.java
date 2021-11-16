package com.quap.client;

import com.quap.client.utils.Prefixes;
import com.quap.client.utils.Suffixes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.HashMap;

public class Client {
    private final HashMap<Prefixes, String> prefixes = new HashMap<>();
    private final HashMap<Suffixes, String> suffixes = new HashMap<>();
    private Socket socket = new Socket();
    private String name;
    private final int port;
    private InetAddress address;
    private int ID = -1;
    private Thread listen;
    BufferedReader reader;
    PrintWriter writer;

    {
        try {
            name = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        for (Prefixes p : Prefixes.values()) {
            prefixes.put(p, "/+" + p.name().toLowerCase().charAt(0) + "+/");
        }
        for (Suffixes s : Suffixes.values()) {
            suffixes.put(s, "/-" + s.name().toLowerCase().charAt(0) + "-/");
        }
    }

    public Client(String address, int port) {
        try {
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = port;
        try {
            socket.bind(new InetSocketAddress(address, port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address.toString();
    }

    public int getPort() {
        return port;
    }

    public boolean openConnection() {
        try {
            socket = new Socket(InetAddress.getByName("192.168.178.69"), 8192); //java.net.ConnectException: Connection refused: connect
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    public void setConnection() {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void authorize(String name, String password) {
        System.out.println("authentication");
        String authenticationMessage = prefixes.get(Prefixes.AUTHENTICATION) +
                name + "|" + password +
                suffixes.get(Suffixes.AUTHENTICATION);
        System.out.println(authenticationMessage);
        sendMessage(authenticationMessage);
    }

    public void disconnect() {
        new Thread(() -> {
            synchronized (socket) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void listen() {
        System.out.println("Client is listen...");
        listen = new Thread(() -> {
            String message;
            while (reader != null) {
                try {
                    message = reader.readLine();
                    if (message.length() > 0) {
                        System.out.println("Incoming message: " + message);
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                    listen.interrupt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        listen.start();
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public String getConnectionInfo() {
        return String.valueOf(socket.getRemoteSocketAddress());
    }

    public void sendMessage(String message) {
        System.out.println("Send Message from Client to Server: " + message);
        writer.println(message);
    }
}
