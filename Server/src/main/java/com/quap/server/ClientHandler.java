package com.quap.server;

import com.quap.data.UserdataReader;
import org.json.JSONArray;
import org.json.JSONException;
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
    private String name;

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
                } catch (IOException e) {
                    e.printStackTrace();
                    this.disconnect();
                }
            }
        });
        listen.start();
    }

    private void process(String message) {                //TODO: run database access as future
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
                assert dbReader != null;
                List<Integer> userIds = new ArrayList<>(dbReader.userIDsByChat(chatID));
                for (Integer id : userIds) {
                    server.forwardMessage(id, content);
                }
            }
            case 'c' -> { //TODO: new chat is inserted into the db but no result is returned
                System.out.println("command found");
                JSONObject data = new JSONObject(content).getJSONObject("data");
                int senderID = data.getInt("sender_id");
                UserdataReader dbReader = null;
                try {
                    dbReader = new UserdataReader();
                } catch (SQLException | URISyntaxException e) {
                    e.printStackTrace();
                }
                switch (new JSONObject(content).getString("type")) {
                    case "create-chat" -> {
                        String chatName = data.getString("chatname");
                        assert dbReader != null;
                        JSONObject result = dbReader.addChat(senderID, chatName, false);
                        JSONObject json = new JSONObject();
                        json.put("return-value", "command");
                        if (result != null) {
                            JSONObject returnValue = new JSONObject();
                            returnValue.put("statement", "create-chat");
                            returnValue.put("result", result);
                            json.put("data", returnValue);
                        } else {
                            json.put("error", "can not create this chat");
                        }
                        send(json.toString());
                    }
                    case "invite-user" -> {
                        System.out.println("Invite user to chat...");
                        try {
                            dbReader = new UserdataReader();
                        } catch (SQLException | URISyntaxException e) {
                            e.printStackTrace();
                        }
                        String username = data.getString("username");
                        String senderName = name;
                        assert dbReader != null;
                        int userID = dbReader.userIDByName(username);
                        int chatID = data.getInt("chat_id");
                        JSONObject chat = dbReader.getChatByID(chatID);
                        JSONArray participants = dbReader.usersByChat(chatID);
                        JSONObject json = new JSONObject();
                        json.put("return-value", "command");
                        if (chat != null) {
                            JSONObject returnValue = new JSONObject();
                            returnValue.put("statement", "invite-chat");
                            returnValue.put("chat", chat);
                            returnValue.put("sender_id", senderID);
                            returnValue.put("sender_name", senderName);
                            returnValue.put("participants", participants);
                            json.put("data", returnValue);
                        } else {
                            json.put("error", "can not create this chat");
                        }
                        server.forwardMessage(userID, json.toString());
                    }
                    case "join-chat" -> {
                        int chatID = data.getInt("chatroom_id");
                        try {
                            dbReader = new UserdataReader();
                        } catch (SQLException | URISyntaxException e) {
                            e.printStackTrace();
                        }
                        assert dbReader != null;
                        dbReader.addUserToChat(chatID, senderID);
                        JSONObject chat = dbReader.getChatByID(chatID);
                        JSONObject json = new JSONObject();
                        json.put("return-value", "command");
                        if (chat != null) {
                            JSONObject returnValue = new JSONObject();
                            returnValue.put("statement", "join-chat");
                            returnValue.put("chat", chat);
                            json.put("data", returnValue);
                        } else {
                            json.put("error", "can not join this chat");
                        }
                        send(json.toString());
                        json = new JSONObject();
                        json.put("return-value", "message");
                        JSONObject returnValue = new JSONObject();
                        returnValue.put("message", "User " + senderID + "joined the chatroom.");
                        returnValue.put("chat_id", chatID);
                        returnValue.put("sender_id", 0); //0 == server
                        json.put("data", returnValue);
                        List<Integer> userIds = new ArrayList<>(dbReader.userIDsByChat(chatID));
                        for (Integer id : userIds) {
                            server.forwardMessage(id, json.toString());
                        }
                    }
                    case "delete-chat" -> {
                        int chatID = data.getInt("chat_id");
                        assert dbReader != null;
                        dbReader.deleteUserFromChat(senderID, chatID);
                        //TODO: send user leave chat message into chat if more than one user is left
                        JSONObject json = new JSONObject();
                        json.put("return-value", "message");
                        JSONObject returnValue = new JSONObject();
                        returnValue.put("message", "User " + senderID + "left the chatroom.");
                        returnValue.put("chat_id", chatID);
                        returnValue.put("sender_id", 0); //0 == server
                        json.put("data", returnValue);
                        List<Integer> userIds = new ArrayList<>(dbReader.userIDsByChat(chatID));
                        for (Integer id : userIds) {
                            server.forwardMessage(id, json.toString());
                        }
                    }
                    case "request-user" -> {
                        String username = data.getString("username");
                        int userID = dbReader.userIDByName(username);
                        String senderName = name;
                        JSONObject json = new JSONObject();
                        json.put("return-value", "command");
                        JSONObject returnValue = new JSONObject();
                        returnValue.put("statement", "friend-request");
                        returnValue.put("sender_id", senderID);
                        returnValue.put("sender_name", senderName);
                        json.put("data", returnValue);
                        server.forwardMessage(userID, json.toString());
                        //TODO: sender ID is already found above only need to get the user by name and request him (live of course)

                    }
                    case "accept-friend" -> {
                        //TODO: make an sql entrance
                        // send a message to both users with the private chat and the friend data
                        int friendID = data.getInt("friend_id");
                        JSONObject json = new JSONObject();
                        json.put("return-value", "command");
                        JSONObject returnValue = new JSONObject();
                        returnValue.put("statement", "add-friend");
                        returnValue.put("private", ""/*TODO*/);
                        /*each friend object:
                            friend.getString("name"),
                            friend.getInt("user_id"),
                            friend.getString("created_at"),
                            friend.getInt("chatrooms_id")
                         */
                        json.put("data", returnValue);
                        List<Integer> userIds = new ArrayList<>(List.of(senderID, friendID));
                        for (Integer id : userIds) {
                            server.forwardMessage(id, json.toString());
                        }
                        JSONObject chat = dbReader.insertFriends(senderID, data.getInt("friend_id"));

                    }
                }
            }
            case 'a' -> {
                System.out.println("authentication found");
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
                JSONObject result;
                if (existing) {
                    result = dbReader.verifyUser(name, password);
                } else {
                    result = dbReader.insertUser(name, password);
                }
                this.name = name;
                try {
                    userID = result.getJSONObject("data").getInt("id");
                } catch (JSONException e) {
                    System.err.println("The user " + name + " was not found.");
                    result = new JSONObject(); //TODO: error result that the user does not exists or the password or username is false
                    result.put("error", "The user " + name + " was not found.");
                    //e.printStackTrace();
                }
                send(result.toString());
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
