package com.quap.client.domain;

import org.json.JSONObject;

import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

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

    public String name() {
        return name;
    }

    public int id() {
        return id;
    }

    public String joined_at() {
        return joined_at;
    }

    public String created_at() {
        return created_at;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Chat) obj;
        return Objects.equals(this.name, that.name) &&
                this.id == that.id &&
                Objects.equals(this.joined_at, that.joined_at) &&
                Objects.equals(this.created_at, that.created_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, joined_at, created_at);
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
        return "Chat[" +
                "name=" + name + ", " +
                "id=" + id + ", " +
                "joined_at=" + joined_at + ", " +
                "created_at=" + created_at + ']';
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Date getTime() {
        return new Date(joined_at);
    }
}