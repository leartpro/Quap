package com.quap.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final int ID;
    private final Socket socket;
    public ClientHandler(Socket socket, int ID) {
        this.socket = socket;
        this.ID = ID;
    }

    @Override
    public void run() {
        InputStream input = null;
        try {
            input = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        OutputStream output = null;
        try {
            output = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter writer = new PrintWriter(output, true);

        String text = null;
        do {
            try {
                text = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String reverseText = new StringBuilder(text).reverse().toString();
            writer.println("Server: " + reverseText);

        } while (!text.equals("bye"));
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
