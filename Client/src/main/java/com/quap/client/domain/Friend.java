package com.quap.client.domain;

import org.json.JSONObject;

import java.util.Date;
import java.util.Objects;

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

    public String name() {
        return name;
    }

    public int id() {
        return id;
    }

    public int chatID() { return chatID; }

    public String created_at() {
        return created_at;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Friend) obj;
        return Objects.equals(this.name, that.name) &&
                this.id == that.id &&
                Objects.equals(this.created_at, that.created_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, created_at);
    }

    @Override
    public String toString() {
        return "Friend[" +
                "name=" + name + ", " +
                "id=" + id + ", " +
                "created_at=" + created_at + ']';
    }

    @Override
    public String display() {
        return "Friend[" +
                "name=" + name + ", " +
                "id=" + id + ", " +
                "created_at=" + created_at + ']';
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Date getTime() {
        return new Date(created_at);
    }
}
