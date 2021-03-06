package com.quap.client;

import com.quap.client.data.UserdataReader;
import com.quap.client.domain.*;
import com.quap.client.utils.ClientObserver;
import com.quap.client.utils.Prefixes;
import com.quap.client.utils.Suffixes;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Client {
    private final HashMap<Prefixes, String> prefixes = new HashMap<>();
    private final HashMap<Suffixes, String> suffixes = new HashMap<>();
    private final int port;
    private final Socket socket;
    private Thread listen;
    InetAddress address;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final ArrayList<Friend> friends = new ArrayList<>();
    private final ArrayList<Chat> chats = new ArrayList<>();
    private int id, chatID;
    private String username;
    private String password;
    private UserdataReader dataReader;
    private final List<ClientObserver> observers = new ArrayList<>();

    {
        try {
            String name = InetAddress.getLocalHost().getHostName();
            System.out.println(name);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        for (Prefixes p : Prefixes.values()) {
            prefixes.put(p, "/+" + p.name().toLowerCase().charAt(0) + "+/");
        }
        for (Suffixes s : Suffixes.values()) {
            suffixes.put(s, "/-" + s.name().toLowerCase().charAt(0) + "-/");
        }
    }

    public Client(String address, int port) throws IOException {
        this.address = InetAddress.getByName(address);
        this.port = port;
        socket = new Socket(InetAddress.getByName("192.168.178.69"), 8192); //java.net.ConnectException: Connection refused: connect
        //socket.bind(null);
        writer = new PrintWriter(socket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        listen();
    }

    public List<UserContent> getFriends() {
        return new ArrayList<>(friends);
    }

    public List<UserContent> getChats() {
        return new ArrayList<>(chats);
    }

    public void authorize(String name, String password, boolean existing) {
        this.username = name;
        this.password = password;
        JSONObject json = new JSONObject();
        json.put("name", this.username);
        json.put("password", this.password);
        json.put("existing", existing);
        sendAuthentication(json.toString());
        //TODO: wait until authentication confirmation is arrived
    }

    public void connectDB() {
        dataReader = new UserdataReader(username, password);
    }

    public void disconnect() {
        //todo: finish the listen thread
        listen.interrupt();
        new Thread(this::closeSocket).start();
        //TODO: send disconnect message to server
        // if anonym mode, clear all data
    }

    public void listen() {
        System.out.println("Client is listen...");
        listen = new Thread(() -> {
            String message;
            while (!socket.isClosed() && reader != null) {
                try {
                    message = reader.readLine(); //java.net.SocketException: Socket closed
                    if (message != null) {
                        process(message);
                    }
                } catch (SocketException se) {
                    System.err.println("Connection closed");
                } catch (IOException e) {
                    e.printStackTrace();
                    listen.interrupt();
                    try {
                        reader.close();
                        writer.close();
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            }
        });
        listen.start();
    }

    private void process(String content) {
        System.out.println(content);
        JSONObject root = new JSONObject(content);
        String returnValue = root.getString("return-value");
        if (!returnValue.equals("void")) {
            if (root.has("error") && !root.has("data")) {
                System.err.println(root.getString("error"));
                //TODO: submit Popup by error event
            } else if (root.has("data")) {
                JSONObject data = root.getJSONObject("data");
                switch (returnValue) {
                    case "authentication" -> {
                        this.id = data.getInt("id");
                        JSONArray chatrooms = data.getJSONArray("chatrooms");
                        for (int i = 0; i < chatrooms.length(); i++) {
                            Chat chat = new Chat(chatrooms.getJSONObject(i));
                            chats.add(chat);
                        }
                        JSONArray privates = data.getJSONArray("private");
                        for (int i = 0; i < privates.length(); i++) {
                            Friend friend = new Friend(privates.getJSONObject(i));
                            friends.add(friend);
                        }
                    }
                    case "message" -> {
                        int senderID = data.getInt("sender_id");
                        int chatID = data.getInt("chat_id");
                        String messageContent = data.getString("message");
                        for (ClientObserver c : observers) {
                            c.messageEvent(new Message(messageContent, new Date(), senderID));
                        }
                        System.out.println("senderID: " + senderID + ", chatID: " + chatID + ", message: " + messageContent);
                        dataReader.addMessage(chatID, senderID, messageContent);
                    }
                    case "command" -> {
                        System.out.println("command found");
                        String statement = data.getString("statement");
                        switch (statement) {
                            case "create-chat" -> {
                                JSONObject result = data.getJSONObject("result");
                                Chat chat = new Chat(
                                        result.getString("name"),
                                        result.getInt("chatroom_id"));
                                chats.add(chat);
                                for (ClientObserver c : observers) {
                                    c.createChatEvent(chat);
                                }
                            }
                            case "invite-chat" -> {
                                JSONObject chatObject = data.getJSONObject("chat");
                                int senderID = data.getInt("sender_id");
                                String senderName = data.getString("sender_name");
                                JSONArray participants = data.getJSONArray("participants");
                                List<String> participantsList = new ArrayList<>();
                                for (int i = 0; i < participants.length(); i++) {
                                    String name = participants.getJSONObject(i).getString("name");
                                    int id = participants.getJSONObject(i).getInt("id");
                                    participantsList.add(name + "#" + id);
                                }
                                Chat chat = new Chat(
                                        chatObject.getString("name"),
                                        chatObject.getInt("id"),
                                        chatObject.getString("created_at")
                                );
                                for (ClientObserver c : observers) {
                                    c.inviteEvent(chat, senderID, senderName, participantsList);
                                }
                            }
                            case "join-chat" -> {
                                JSONObject chatObject = data.getJSONObject("chat");
                                Chat chat = new Chat(
                                        chatObject.getString("name"),
                                        chatObject.getInt("id"),
                                        chatObject.getString("created_at")
                                );
                                chats.add(chat);
                                for (ClientObserver c : observers) {
                                    c.joinChatEvent(chat);
                                }
                            }
                            case "friend-request" -> {
                                //JSONObject chatObject = data.getJSONObject("chat");
                                int senderID = data.getInt("sender_id");
                                String senderName = data.getString("sender_name");
                                Friend friend = new Friend(
                                        senderName, senderID
                                );
                                for (ClientObserver c : observers) {
                                    c.friendRequestEvent(friend);
                                }
                            }
                            case "add-friend" -> {
                                Friend friend = new Friend(data.getJSONObject("friend"));
                                friends.add(friend);
                                for (ClientObserver c : observers) {
                                    c.addFriendEvent(friend);
                                }
                            }
                        }
                    }
                }
            } else {
                System.err.println("Unknown package content");
            }
        } else {
            System.out.println("No data expected");
        }
    }


    public String getConnectionInfo() {
        return address.getCanonicalHostName()+":"+port + " --> " + socket.getRemoteSocketAddress();
    }

    public void sendMessage(String message) {
        JSONObject json = new JSONObject();
        json.put("return-value", "message");
        JSONObject data = new JSONObject();
        data.put("message", message);
        data.put("chat_id", chatID);
        data.put("sender_id", id);
        json.put("data", data);
        String output = prefixes.get(Prefixes.MESSAGE) + json + suffixes.get(Suffixes.MESSAGE);
        System.out.println("Send Message from Client to Server: \n" + output);
        writer.println(output);
    }

    public void sendAuthentication(String authentication) {
        String output = prefixes.get(Prefixes.AUTHENTICATION) + authentication + suffixes.get(Suffixes.AUTHENTICATION);
        System.out.println("Send Message from Client to Server: \n" + authentication);
        writer.println(output);
    }

    public void sendCommand(String command) {
        String output = prefixes.get(Prefixes.COMMAND) + command + suffixes.get(Suffixes.COMMAND);
        System.out.println("Send Message from Client to Server: \n" + command);
        writer.println(output);
    }

    private void closeSocket() {
        synchronized (socket) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Content> getMessagesByChat(int id) {
        return new ArrayList<>(
                dataReader.getMessagesByChat(id)
        );
    }

    public void setCurrentChatID(int chatID) {
        this.chatID = chatID;
    }

    public void addObserver(ClientObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ClientObserver observer) {
        observers.remove(observer);
    }

    public void addChatroom(String input) {
        JSONObject json = new JSONObject();
        json.put("type", "create-chat");
        JSONObject data = new JSONObject();
        data.put("chatname", input);
        data.put("sender_id", id);
        json.put("data", data);
        sendCommand(json.toString());
    }

    public void addFriend(String input) { //TODO: similar to method addChatroom()
        JSONObject json = new JSONObject();
        json.put("type", "request-user");
        JSONObject data = new JSONObject();
        data.put("username", input);
        data.put("sender_id", id);
        json.put("data", data);
        sendCommand(json.toString());
    }

    public void joinChat(int chatID) {
        JSONObject json = new JSONObject();
        json.put("type", "join-chat");
        JSONObject data = new JSONObject();
        data.put("chatroom_id", chatID);
        data.put("sender_id", id);
        json.put("data", data);
        sendCommand(json.toString());
    }

    public void inviteUser(String username, int chatID) {
        JSONObject json = new JSONObject();
        json.put("type", "invite-user");
        JSONObject data = new JSONObject();
        data.put("username", username);
        data.put("chat_id", chatID);
        data.put("sender_id", id);
        json.put("data", data);
        sendCommand(json.toString());
    }

    public void deleteChat(Chat chat) {
        chats.remove(chat);
        //TODO: delete messages by chat
        JSONObject json = new JSONObject();
        json.put("type", "delete-chat");
        JSONObject data = new JSONObject();
        data.put("chat_id", chat.id());
        data.put("sender_id", id);
        json.put("data", data);
        sendCommand(json.toString());
    }

    public void acceptFriend(int id) {
        JSONObject json = new JSONObject();
        json.put("type", "accept-friend");
        JSONObject data = new JSONObject();
        //data.put("username", username);
        data.put("friend_id", id);
        data.put("sender_id", this.id);
        json.put("data", data);
        sendCommand(json.toString());
    }
}