package com.quap.server;

import com.quap.data.UserdataReader;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class ClientHandler implements Runnable {
    private final int ID;
    private final Socket socket;
    private Thread listen; //because listen is the only method call in run(), the clientHandler will quit when the listen Thread is interrupted

    private InputStream input;
    private final BufferedReader reader;
    private OutputStream output;
    private final PrintWriter writer;

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
    }

    @Override
    public void run() {
        listen();
        disconnect();
    }

    private void listen() {
        listen = new Thread(() -> {
            while(!socket.isClosed()) {
                    try {
                        String message = reader.readLine();
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
        String content = message.substring(5, (message.length()-5));
        System.out.println(message);
        System.out.println(content);
        switch(message.charAt(2)) {
            case 'm' -> System.out.println("message found");
            case 'c' -> System.out.println("command found");
            case 'a' -> {
                System.out.println("authentication found");
                //TODO: run database access as future
                UserdataReader dbReader = null;
                try {
                    dbReader = new UserdataReader();
                } catch (SQLException | URISyntaxException e) {
                    e.printStackTrace();
                }
                assert dbReader != null;
                JSONObject json = new JSONObject(content);
                String name = json.getString("name");
                String password = json.getString("password");
                boolean existing = json.getBoolean("existing");

                String result;
                if(existing) {
                    result = dbReader.verifyUser(name, password);
                } else {
                    result = dbReader.insertUser(name, password);
                }
                send(result);
            }
        }
    }

    private void send(String message) {
        System.out.println("Server Message to Client: " + message);
        writer.println(message);
    }

    public int getID() {
        return ID;
    }

    public ServerClient getClient() {
        return null;
    }

    public void disconnect() {
        System.out.println("Disconnecting");
    }
}
