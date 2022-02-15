package com.quap.client.domain;

import org.json.JSONObject;

import java.time.LocalTime;

public final class Chat extends UserContent {
    private final String name;
    private final int id;
    private final String joined_at;
    private final String created_at;

    public Chat(String name, int id, String joined_at, String created_at) {
        this.name = name;
        this.id = id;
        this.joined_at = joined_at;
        this.created_at = created_at;
    }

    public Chat(JSONObject chat) {
        this(
                chat.getString("name"),
                chat.getInt("id"),
                chat.getString("joined_at"),
                chat.getString("created_at")
        );
    }

    public Chat(String name, int id) {
        this(
                name,
                id,
                LocalTime.now().toString(),
                LocalTime.now().toString()
        );
    }

    public Chat(String name, int id, String created_at) {
        this(
                name,
                id,
                LocalTime.now().toString(),
                created_at
        );
    }

    @Override
    public int chatID() { return id; }

    @Override
    public int id() {
        return id;
    }

    @Override
    public String toString() {
        return "Chat[" +
                "name=" + name + ", " +
                "id=" + id + ", " +
                "joined_at=" + joined_at + ", " +
                "created_at=" + created_at + ']';
    }

    @Override
    public String display() {
        return "Chat: " + name;
    }

    @Override
    public String content() {
        return name;
    }
}