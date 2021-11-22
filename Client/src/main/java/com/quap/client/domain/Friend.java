package com.quap.client.domain;

import org.json.JSONObject;

import java.util.Objects;

public final class Friend extends UserContent {
    private final String name;
    private final int id;
    private final String created_at;

    public Friend(String name, int id, String created_at) {
        this.name = name;
        this.id = id;
        this.created_at = created_at;
    }

    public Friend(JSONObject chat) {
        this(
                chat.getString("name"),
                chat.getInt("id"),
                chat.getString("created_at")
        );
    }

    public String name() {
        return name;
    }

    public int id() {
        return id;
    }

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
        return 0;
    }

    @Override
    public String getTime() {
        return null;
    }
}
