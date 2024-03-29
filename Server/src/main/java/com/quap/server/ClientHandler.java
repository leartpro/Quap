package com.quap.server;

import com.quap.data.UserdataReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Klasse ist für die eingehenden Anfragen des übergebenen Clients zuständig.
 * Die ClientHandler laufen Nebenläufig voneinander ab und werden im Server verwaltet
 */
public class ClientHandler implements Runnable {
    private final int ID;
    private final UserdataReader dbReader;
    private int userID;
    private final Socket socket;
    private final Server server;
    private Thread listen;

    private InputStream input;
    private final BufferedReader reader;
    private OutputStream output;
    private final PrintWriter writer;
    private String name;

    /**
     * Im Konstruktor werden die Streams der Socket Writer und Reader zugewiesen.
     * @param socket Die Socket, welche bei einer eingehenden Verbindung erzeugt wurde
     * @param ID eine UUID, generiert bei der UniqueIdentifier Klasse
     * @param server eine Referenz auf den Server
     */
    public ClientHandler(Socket socket, int ID, Server server, UserdataReader dbConnector) {
        this.socket = socket;
        this.ID = ID;
        this.server = server;
        this.dbReader = dbConnector;
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

    /**
     * Die Methode wirft einen Thread aus, welcher den eingehenden Stream liest
     */
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
                    this.disconnect(false);
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    this.disconnect(false);
                }
            }
        });
        listen.start();
    }

    /**
     * Diese Methode differenziert die einkommenden Daten anhand ihrer Metadaten
     * und führt für verschiedene Werte unterschiedliche Aktionen aus.
     * @param message den Input des eingehenden Streams
     */
    private void process(String message) {
        String content = message.substring(5, (message.length() - 5));
        System.out.println(message);
        System.out.println(content);
        switch (message.charAt(2)) {
            case 'm' -> {
                System.out.println("message found:" + content);
                JSONObject input = new JSONObject(content).getJSONObject("data");
                int chatID = input.getInt("chat_id");
                assert dbReader != null;
                List<Integer> userIds = new ArrayList<>(dbReader.userIDsByChat(chatID));
                for (Integer id : userIds) {
                    server.forwardMessage(id, content);
                }
            }
            case 'c' -> {
                System.out.println("command found");
                JSONObject data = new JSONObject(content).getJSONObject("data");
                int senderID = data.getInt("sender_id");
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
                        String username = data.getString("username");
                        String senderName = name;
                        assert dbReader != null;
                        int userID = dbReader.userIDByName(username);
                        int chatID = data.getInt("chat_id");
                        boolean isParticipant = false;
                        for(int participant : dbReader.userIDsByChat(chatID)) {
                            if (userID == participant) {
                                isParticipant = true;
                                break;
                            }
                        }
                        if(!isParticipant) {
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
                    }
                    case "join-chat" -> {
                        int chatID = data.getInt("chatroom_id");
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
                        returnValue.put("message", "User " + dbReader.userNameById(senderID) + " joined the chatroom.");
                        returnValue.put("chat_id", chatID);
                        returnValue.put("sender_id", 0);
                        returnValue.put("sender_name", "Server");
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
                        JSONObject json = new JSONObject();
                        json.put("return-value", "message");
                        JSONObject returnValue = new JSONObject();
                        returnValue.put("message", "User " + dbReader.userNameById(senderID) + " left the chatroom.");
                        returnValue.put("chat_id", chatID);
                        returnValue.put("sender_id", 0); //0 == server
                        returnValue.put("sender_name", "Server");
                        json.put("data", returnValue);
                        List<Integer> userIds = new ArrayList<>(dbReader.userIDsByChat(chatID));
                        for (Integer id : userIds) {
                            server.forwardMessage(id, json.toString());
                        }
                    }
                    case "request-user" -> {
                        String username = data.getString("username");
                        assert dbReader != null;
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
                    }
                    case "accept-friend" -> {
                        int friendID = data.getInt("friend_id");
                        assert dbReader != null;
                        int chatID = dbReader.insertFriends(senderID, friendID);
                        JSONObject json = new JSONObject();
                        json.put("return-value", "command");
                        JSONObject returnValue = new JSONObject();
                        returnValue.put("statement", "add-friend");
                        JSONObject friend = new JSONObject();
                        friend.put("name", dbReader.userNameById(friendID));
                        friend.put("user_id", friendID);
                        friend.put("created_at", LocalTime.now().toString());
                        friend.put("chatrooms_id", chatID);
                        returnValue.put("friend", friend);
                        json.put("data", returnValue);
                        server.forwardMessage(senderID, json.toString());

                        friend = new JSONObject();
                        returnValue.remove("friend");
                        friend.put("name", dbReader.userNameById(senderID));
                        friend.put("user_id", userID);
                        friend.put("created_at", LocalTime.now().toString());
                        friend.put("chatrooms_id", chatID);
                        returnValue.put("friend", friend);
                        json.put("data", returnValue);
                        server.forwardMessage(friendID, json.toString());
                    }
                    case "unfriend-user" -> {
                        int friendID = data.getInt("friend_id");
                        int chatID = data.getInt("chat_id");
                        assert dbReader != null;
                        dbReader.unfriendUsers(chatID);
                        JSONObject json = new JSONObject();
                        json.put("return-value", "command");
                        JSONObject returnValue = new JSONObject();
                        returnValue.put("statement", "delete-friend");
                        returnValue.put("friend_id", senderID);
                        returnValue.put("chat_id", chatID);
                        json.put("data", returnValue);
                        server.forwardMessage(friendID, json.toString());
                    }
                }
            }
            case 'a' -> {
                System.out.println("authentication found");
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
                    result = new JSONObject();
                    result.put("error", "The user " + name + " was not found.");
                    result.put("return-value", "authentication");
                }
                send(result.toString());
            }
            case 'd' -> {
                System.out.println("disconnect found");
                assert dbReader != null;
                listen.interrupt();
                dbReader.disconnect();
            }
        }
    }

    /**
     * Die Methode sendet beim Aufruf, über den Writer, die Nachricht zum Client
     * @param message die Nachricht im JSON-Format, mit allen relevanten Metadaten
     */
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

    /**
     * Teilt beim Methodenaufruf den Verbindungsabbruch dem Client mit
     * @param status false bei Fehler
     */
    public void disconnect(boolean status) {
        JSONObject json = new JSONObject();
        json.put("return-value", "disconnect");
        JSONObject data = new JSONObject();
        data.put("status", status);
        json.put("data", data);
        send(json.toString());
    }

    /**
     * Startet die listen-Methode und wird durch das Runnable Interface vorgeschrieben
     */
    @Override
    public void run() {
        listen();
    }
}
