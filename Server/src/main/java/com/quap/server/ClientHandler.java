package com.quap.server;

import com.quap.data.UserdataReader;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ClientHandler implements Callable {
    private final int ID;
    private int userID;
    private final Socket socket;
    private final Server server;
    private Thread listen; //because listen is the only method call in run(), the clientHandler will quit when the listen Thread is interrupted

    private InputStream input;
    private final BufferedReader reader;
    private OutputStream output;
    private final PrintWriter writer;

    public ClientHandler(Socket socket, int ID, Server server) {
        this.socket = socket;
        this.ID = ID;
        this.server = server;
        userID = -1;

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

    private void listen() {
        listen = new Thread(() -> {
            String message;
            while (!socket.isClosed() && reader != null) {
                try {
                    message = reader.readLine();
                    if (message != null) {
                        process(message);
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                    listen.interrupt();
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                    this.disconnect();
                }
            }
        });
        listen.start();
    }

    private void process(String message) {
        String content = message.substring(5, (message.length() - 5));
        System.out.println(message);
        System.out.println(content);
        switch (message.charAt(2)) {
            case 'm' -> {
                System.out.println("message found:" + content);
                UserdataReader dbReader = null;
                try {
                    dbReader = new UserdataReader();
                } catch (SQLException | URISyntaxException e) {
                    e.printStackTrace();
                }

                JSONObject input = new JSONObject(content).getJSONObject("data");
                int chatID = input.getInt("chat_id");
                //TODO: receive message status success, rejected, lost, etc.
                List<Integer> userIds = new ArrayList<>(dbReader.userIDsByChat(chatID));
                for (Integer id : userIds) {
                    //if(id != userID) { //dont sends to himself
                        server.forwardMessage(id, content);
                    //}
                }
            }
            case 'c' -> {
                System.out.println("command found");
            }
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
                if (existing) {
                    result = dbReader.verifyUser(name, password);
                } else {
                    result = dbReader.insertUser(name, password);
                }
                userID = new JSONObject(result).getJSONObject("data").getInt("id");

                send(result);
                //TODO: set userID here
            }
        }
    }

    public void send(String message) {
        System.out.println("Server Message to Client: " + message);
        writer.println(message);
    }

    public int getID() {
        return ID;
    }

    public int getUserID() {
        return userID;
    }

    public ServerClient getClient() {
        return null;
    }

    public void disconnect() {
        System.out.println("Disconnecting");
    }

    @Override
    public ServerClient call() throws Exception {
        listen();
        //disconnect(); is called direct after listen()

        return null;
    }
}
