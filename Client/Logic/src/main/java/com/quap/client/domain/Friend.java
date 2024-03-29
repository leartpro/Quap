package com.quap.client.domain;

import org.json.JSONObject;

import java.time.LocalTime;

public final class Friend extends UserContent {
    private final String name;
    private final int id;
    private final String created_at;
    private final int chatID;

    public Friend(String name, int id, String created_at, int chatID) {
        this.name = name;
        this.id = id;
        this.created_at = created_at;
        this.chatID = chatID;
    }

    public Friend(JSONObject friend) {
        this(
                friend.getString("name"),
                friend.getInt("user_id"),
                friend.getString("created_at"),
                friend.getInt("chatrooms_id")
        );
    }

    public Friend(String name, int id) {
        this(
                name,
                id,
                LocalTime.now().toString(),
                -1
        );
    }

    public String name() {
        return name;
    }

    @Override
    public int id() { return id; }

    @Override
    public int chatID() { return chatID; }

    @Override
    public String toString() {
        return "Friend[" +
                "name=" + name + ", " +
                "id=" + id + ", " +
                "created_at=" + created_at + ']';
    }

    @Override
    public String display() {
        return "Friend: " + name;
    }

    @Override
    public String content() {
        return name;
    }
}
