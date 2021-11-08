package com.quap.client;

import com.quap.client.network.Prefixes;
import com.quap.client.network.Suffixes;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class Client {
    private final HashMap<Prefixes, String> prefixes = new HashMap();
    private final HashMap<Suffixes, String> suffixes = new HashMap();
    private Socket socket;
    private String name;
    private final int port;
    private Thread send, listen;
    private InetAddress address;
    private int ID = -1;

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
    }

    public Client(String address, int port) {
        socket = new Socket();
        try {
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
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

    public void send(String text) {
        send = new Thread("Send") {
            public void run() {

            }
        };
        send.start();
    }

    public String receive() {
        String message = "";
        return message;
    }

    public boolean openConnection() {
        try {
            //java.net.BindException: Cannot assign requested address: connect
            //socket = new Socket(InetAddress.getByName("de.quap.com"), remotePort, InetAddress.getLocalHost(), 8080); //TODO: add local configurations

            //try2
            socket.bind(new InetSocketAddress(address, port));
            socket = new Socket(InetAddress.getByName("192.168.56.1"), 8192); //java.net.ConnectException: Connection refused: connect
            /*
            This exception means that there is no service listening on the IP/port you are trying to connect to:
                - You are trying to connect to the wrong IP/Host or port.
                - You have not started your server.
                - Your server is not listening for connections.
                - On Windows servers, the listen backlog queue is full.
                */
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
                while (true) {
                    String message = receive();
                    if (message.startsWith("/c/")) {
                        setID(Integer.parseInt(message.split("/c/|/e/")[1]));
                        System.out.println("Successfully connected to server! ID: " + getID());
                    } else if (message.startsWith("/m/")) {
                        String text = message.substring(3);
                        text = text.split("/e/")[0];
                        System.out.println(text);
                    } else if (message.startsWith("/i/")) {
                        String text = "/i/" + getID() + "/e/";
                        send(text);
                    } else if (message.startsWith("/u/")) {
                        String[] u = message.split("/u/|/n/|/e/");
                        //users.update(Arrays.copyOfRange(u, 1, u.length - 1));
                    }
                }
            }
        };
        listen.start();
    }

    //public void update(String[] users) {
    //    list.setListData(users);
    //}

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public String getConnectionInfo() {
        return String.valueOf(socket.getRemoteSocketAddress());
    }
}
