package com.quap.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final int ID;
    private final Socket socket;
    InputStream input;
    BufferedReader reader;
    OutputStream output;
    PrintWriter writer;

    public ClientHandler(Socket socket, int ID) {
        this.socket = socket;
        this.ID = ID;

        try {
            input = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            output = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        writer = new PrintWriter(output, true);
        reader = new BufferedReader(new InputStreamReader(input));
        System.out.println("new Handler");
    }

    private void listen() {
        System.out.println("Handler is listen...");
        new Thread(() -> {
            String message = null;
            while(!socket.isClosed()) {
                do {
                    System.out.println("Test");
                    try {
                        message = reader.readLine();
                        send("Return from Server");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } while(!message.equals("bye"));
            }
        }).start();
    }

    private void send(String message) {
        System.out.println("Server Message to Client: " + message);
        writer.println(message);
    }

    @Override
    public void run() {
        listen();
    }

    public int getID() {
        return ID;
    }

    public ServerClient getClient() {
        return null;
    }

    public void disconnect() {
    }
}
