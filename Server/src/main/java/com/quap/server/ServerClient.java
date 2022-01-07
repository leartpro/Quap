package com.quap.server;

import java.net.InetAddress;

public class ServerClient {
    public final String name;
    public final InetAddress address;
    public final int port;
    private final int ID;
    public int attempt = 0;

    public ServerClient(String name, InetAddress address, int port, final int ID) {
        this.name = name;
        this.address = address;
        this.port = port;
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }
}
