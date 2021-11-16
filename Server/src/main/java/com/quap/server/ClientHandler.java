package com.quap.server;

import com.quap.data.UserdataReader;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class ClientHandler implements Runnable {
    private final int ID;
    private final Socket socket;
    private Thread listen;
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
        listen = new Thread(() -> {
            String message = null;
            while(!socket.isClosed()) {
                    try {
                        message = reader.readLine();
                        //send("Return from Server");
                        if(message.length() > 0) {
                            process(message);
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

    private void process(String message) {
        System.out.println(message);
        String content = message.substring(5, (message.length()-5)); //TODO: begin -1, end -1, length 16
        System.out.println(content);
        switch(message.charAt(2)) {
            case 'm' -> System.out.println("message found");
            case 'c' -> System.out.println("command found");
            case 'a' -> {
                //TODO: run database access as future
                UserdataReader dbReader = null;
                try {
                    dbReader = new UserdataReader();
                } catch (SQLException | URISyntaxException e) {
                    e.printStackTrace();
                }
                assert dbReader != null;
                System.out.println(content.split("\\|")[0] + " : "  + content.split("\\|")[1]);
                boolean success = dbReader.insertUser(content.split("\\|")[0], content.split("\\|")[1]);
                System.out.println(success);
            }
        }
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
