package com.quap.client;

import com.quap.client.data.UserdataReader;
import com.quap.client.domain.Chat;
import com.quap.client.domain.Friend;
import com.quap.client.domain.Message;
import com.quap.client.utils.Prefixes;
import com.quap.client.utils.Suffixes;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Client {
    private final HashMap<Prefixes, String> prefixes = new HashMap<>();
    private final HashMap<Suffixes, String> suffixes = new HashMap<>();
    private Socket socket = new Socket();
    private String name;
    private final int port;
    private final InetAddress address;
    private Thread listen;
    private BufferedReader reader;
    private PrintWriter writer;
    private final List<Friend> friends = new ArrayList();
    private final List<Chat> chats = new ArrayList();
    private int id;
    private String username;
    private String password;
    UserdataReader dataReader;

    {
        try {
            name = InetAddress.getLocalHost().getHostName();
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
        socket.bind(new InetSocketAddress(address, port));
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public List<Chat> getChats() {
        return  chats;
    }

    public void openConnection() throws IOException {
        socket = new Socket(InetAddress.getByName("192.168.178.69"), 8192); //java.net.ConnectException: Connection refused: connect
    }

    public void setConnection() throws IOException {
        writer = new PrintWriter(socket.getOutputStream(), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

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

    public void disconnect() {
        new Thread(this::closeSocket).start();
        /*new Thread(() -> {
            synchronized (socket) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }

    public void listen() {
        System.out.println("Client is listen...");
        listen = new Thread(() -> {
            String message = null;
            while (!socket.isClosed() && reader != null) {
                try {
                    message = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                    listen.interrupt();
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (message == null) {
                    continue;
                }
                process(message);
            }
        });
        listen.start();
    }

    private void process(String content) {
        System.out.println(content);
        JSONObject root = new JSONObject(content);
        String returnValue = root.getString("return-value");
        if(!returnValue.equals("void")) {
            if (root.has("error") && !root.has("data")) {
                System.out.println(root.getJSONObject("error"));
            } else if (root.has("data")) {
                JSONObject data = root.getJSONObject("data");
                if (returnValue.equals("authentication")) {
                    this.id = data.getInt("id");
                    JSONArray chatrooms = data.getJSONArray("chatrooms");
                    for (int i = 0; i < chatrooms.length(); i++) {
                        Chat chat = new Chat(chatrooms.getJSONObject(i));
                        chats.add(chat);
                    }
                    JSONArray privates = data.getJSONArray("private");
                    for (int i = 0; i < privates.length(); i++) {
                        Friend friend = new Friend(chatrooms.getJSONObject(i));
                        friends.add(friend);
                    }
                    //TODO: load Content in the UI
                } else if (returnValue.equals("message")) {
                    int senderID = data.getInt("sender-id");
                    int chatID = data.getInt("chat-id");
                    String messageContent = data.getString("message");
                    //TODO: load Content in the UI
                } else if(returnValue.equals("command")) {

                }
            } else {
                System.out.println("Unknown package content");
            }
        } else {
            System.out.println("No data expected");
        }
    }


    public String getConnectionInfo() {
        return String.valueOf(socket.getRemoteSocketAddress());
    }

    public void sendMessage(String message) {
        String output = prefixes.get(Prefixes.MESSAGE) + message + suffixes.get(Suffixes.MESSAGE);
        System.out.println("Send Message from Client to Server: \n" + message);
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

    public List<Message> getMessagesByChat(int id) {
        return dataReader.getMessagesByChat(id);
    }
}
