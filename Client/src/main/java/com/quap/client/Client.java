package com.quap.client;

import com.quap.cryptographic.Prefixes;
import com.quap.cryptographic.Suffixes;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class Client {
    private static final HashMap<Prefixes, String> prefixes = new HashMap();
    private static final HashMap<Suffixes, String> suffixes = new HashMap();
    private Socket socket;
    private static final int remotePort;


    static {
        for(Prefixes p : Prefixes.values()) {
            prefixes.put(p, "/+"+ p.name().toLowerCase().charAt(0)+ "+/");
        }

        for(Suffixes s : Suffixes.values()) {
            suffixes.put(s, "/-"+ s.name().toLowerCase().charAt(0)+ "-/");
        }
        remotePort = 80;
    }

    public Client(String name, InetAddress address, int port) {
        try {
            socket = new Socket(name, remotePort, address, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        openConnection();
    }

    public void send() {

    }

    public void receive() {

    }

    public void openConnection() {

    }
}
