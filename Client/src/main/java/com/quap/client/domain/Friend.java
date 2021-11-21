package com.quap.client.domain;

import org.json.JSONObject;

public record Friend(String name, int id, String created_at) {
    public Friend(JSONObject chat) {
        this(
                chat.getString("name"),
                chat.getInt("id"),
                chat.getString("created_at")
        );
    }
}
