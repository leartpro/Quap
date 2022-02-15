package com.quap.client;

import com.quap.client.data.UserdataReader;
import com.quap.client.domain.*;
import com.quap.client.utils.LoginClientObserver;
import com.quap.client.utils.MainClientObserver;
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

/**
 * TODO
 */
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
    private final List<MainClientObserver> mainClientObservers = new ArrayList<>();
    private final List<LoginClientObserver> loginClientObservers = new ArrayList<>();

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

    /**
     * TODO
     * @param address
     * @param port
     * @throws IOException
     */
    public Client(String address, int port) throws IOException {
        this.address = InetAddress.getByName(address);
        this.port = port;
        socket = new Socket(InetAddress.getByName("192.168.178.69"), 8192);
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

    public String getUsername() {
        return username;
    }

    /**
     * TODO
     * @param name
     * @param password
     * @param existing
     */
    public void authorize(String name, String password, boolean existing) {
        this.username = name;
        this.password = password;
        JSONObject json = new JSONObject();
        json.put("name", this.username);
        json.put("password", this.password);
        json.put("existing", existing);
        sendAuthentication(json.toString());
    }

    public void connectDB() {
        dataReader = new UserdataReader(username, password);
    }

    /**
     * TODO
     * @param status
     */
    public void disconnect(boolean status) {
        sendDisconnect(status);
        listen.interrupt();
        new Thread(this::closeSocket).start();
    }

    /**
     * TODO
     */
    public void listen() {
        listen = new Thread(() -> {
            String message;
            while (!socket.isClosed() && reader != null) {
                try {
                    message = reader.readLine();
                    if (message != null) {
                        process(message);
                    }
                } catch (SocketException se) {
                    disconnect(false);
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

    /**
     * TODO
     * @param content
     */
    private void process(String content) {
        System.out.println(content);
        JSONObject root = new JSONObject(content);
        String returnValue = root.getString("return-value");
        if (!returnValue.equals("void")) {
            if (root.has("error") && !root.has("data")) {
                System.err.println(root.getString("error"));
                if ("authentication".equals(returnValue)) {
                    for (LoginClientObserver c : loginClientObservers) {
                        c.authFailedEvent(root.getString("error"));
                    }
                }
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
                        LoginClientObserver r = null;
                        for(LoginClientObserver c : loginClientObservers) {
                            c.authSuccessEvent();
                            r = c;
                        }
                        removeLoginObserver(r);
                    }
                    case "message" -> {
                        int senderID = data.getInt("sender_id");
                        String senderName = data.getString("sender_name");
                        int chatID = data.getInt("chat_id");
                        String messageContent = data.getString("message");
                        for (MainClientObserver c : mainClientObservers) {
                            c.messageEvent(new Message(messageContent, new Date(), senderID, senderName));
                        }
                        System.out.println("senderID: " + senderID + ", chatID: " + chatID + ", message: " + messageContent);
                        dataReader.addMessage(chatID, senderID, senderName, messageContent);
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
                                for (MainClientObserver c : mainClientObservers) {
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
                                for (MainClientObserver c : mainClientObservers) {
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
                                for (MainClientObserver c : mainClientObservers) {
                                    c.joinChatEvent(chat);
                                }
                            }
                            case "friend-request" -> {
                                int senderID = data.getInt("sender_id");
                                String senderName = data.getString("sender_name");
                                Friend friend = new Friend(
                                        senderName, senderID
                                );
                                for (MainClientObserver c : mainClientObservers) {
                                    c.friendRequestEvent(friend);
                                }
                            }
                            case "add-friend" -> {
                                Friend friend = new Friend(data.getJSONObject("friend"));
                                friends.add(friend);
                                for (MainClientObserver c : mainClientObservers) {
                                    c.addFriendEvent(friend);
                                }
                            }
                            case "delete-friend" -> {
                                friends.removeIf(friend -> friend.id() == data.getInt("friend_id"));
                                for(Friend friend : friends) {
                                    if(friend.id() == data.getInt("friend_id")) {
                                        friends.remove(friend);
                                        dataReader.deleteMessagesByChat(friend.chatID());
                                    }
                                }
                                for (MainClientObserver c : mainClientObservers) {
                                    c.unfriendEvent();
                                }
                            }
                        }
                    }
                    case "disconnect" -> {
                        for (MainClientObserver c : mainClientObservers) {
                            c.serverDisconnectEvent("The server does not respond anymore and has disconnected with the status '" + data.getString("status") + "'", "Warning");
                        }
                        listen.interrupt();
                        new Thread(this::closeSocket).start();
                    }
                }
            } else {
                System.err.println("Unknown package content");
            }
        }
    }


    /**
     * TODO
     * @return
     */
    public String getConnectionInfo() { //TODO: fix local addr
        return address.getCanonicalHostName()+":"+port + " --> " + socket.getRemoteSocketAddress();
    }

    /**
     * TODO
     * @param message
     */
    public void sendMessage(String message) {
        JSONObject json = new JSONObject();
        json.put("return-value", "message");
        JSONObject data = new JSONObject();
        data.put("message", message);
        data.put("chat_id", chatID);
        data.put("sender_id", id);
        data.put("sender_name", username);
        json.put("data", data);
        String output = prefixes.get(Prefixes.MESSAGE) + json + suffixes.get(Suffixes.MESSAGE);
        writer.println(output);
    }

    /**
     * TODO
     * @param authentication
     */
    public void sendAuthentication(String authentication) {
        String output = prefixes.get(Prefixes.AUTHENTICATION) + authentication + suffixes.get(Suffixes.AUTHENTICATION);
        writer.println(output);
    }

    /**
     * TODO
     * @param command
     */
    public void sendCommand(String command) {
        String output = prefixes.get(Prefixes.COMMAND) + command + suffixes.get(Suffixes.COMMAND);
        writer.println(output);
    }

    /**
     * TODO
     * @param status
     */
    private void sendDisconnect(boolean status) {
        JSONObject json = new JSONObject();
        json.put("status", status);
        String output = prefixes.get(Prefixes.DISCONNECT) + json + suffixes.get(Suffixes.DISCONNECT);
        writer.println(output);
    }

    /**
     * TODO
     */
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

    public void addMainObserver(MainClientObserver observer) {
        mainClientObservers.add(observer);
    }

    public void removeMainObserver(MainClientObserver observer) {
        mainClientObservers.remove(observer);
    }

    public void addLoginObserver(LoginClientObserver observer) {
        loginClientObservers.add(observer);
    }

    public void removeLoginObserver(LoginClientObserver observer) {
        loginClientObservers.remove(observer);
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

    public void addFriend(String input) {
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
        dataReader.deleteMessagesByChat(chat.id());
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

    public void unfriendUser(Friend friend) {
        friends.remove(friend);
        dataReader.deleteMessagesByChat(friend.chatID());
        JSONObject json = new JSONObject();
        json.put("type", "unfriend-user");
        JSONObject data = new JSONObject();
        data.put("friend_id", friend.id());
        data.put("sender_id", id);
        data.put("chat_id", friend.chatID());
        json.put("data", data);
        sendCommand(json.toString());
    }
}