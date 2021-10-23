package com.quap.client;

import com.quap.client.network.Prefixes;
import com.quap.client.network.Suffixes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;

public class Client {
    private final HashMap<Prefixes, String> prefixes = new HashMap();
    private final HashMap<Suffixes, String> suffixes = new HashMap();
    private Socket socket;
    private final int remotePort;
    private String name;
    private final int port;
    private final InetAddress address;

    {
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
    }

    public Client(InetAddress address, int port) {
        this.address = address;
        this.port = port;
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

    public void send() {
        send = new Thread("Send") {
            public void run() {
                DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        send.start();
    }

    public String receive() {
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String message = new String(packet.getData());
        return message;
    }

    public void openConnection() {
        try {
            //socket = new Socket(InetAddress.getByName("de.quap.com"), 8192, address, port); //TODO: change socket
            socket = new Socket(InetAddress.getByName("de.quap.com"), 8192); //TODO: add local configurations
            //socket.bind(new InetSocketAddress(address, port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void authorize() {
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
        listen = new Thread("Listen") {
            public void run() {
                while (running) {
                    String message = client.receive();
                    if (message.startsWith("/c/")) {
                        client.setID(Integer.parseInt(message.split("/c/|/e/")[1]));
                        console("Successfully connected to server! ID: " + client.getID());
                    } else if (message.startsWith("/m/")) {
                        String text = message.substring(3);
                        text = text.split("/e/")[0];
                        console(text);
                    } else if (message.startsWith("/i/")) {
                        String text = "/i/" + client.getID() + "/e/";
                        send(text, false);
                    } else if (message.startsWith("/u/")) {
                        String[] u = message.split("/u/|/n/|/e/");
                        users.update(Arrays.copyOfRange(u, 1, u.length - 1));
                    }
                }
            }
        };
        listen.start();
    }

    public void update(String[] users) {
        list.setListData(users);
    }
}
