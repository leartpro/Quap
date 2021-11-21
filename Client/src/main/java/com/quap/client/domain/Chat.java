package com.quap.client.domain;

import org.json.JSONObject;

public record Chat(String name, int id, String joined_at, String created_at) {
    public Chat(JSONObject chat) {
        this(
                chat.getString("name"),
                chat.getInt("id"),
                chat.getString("joined_at"),
                chat.getString("created_at")
        );
    }
}