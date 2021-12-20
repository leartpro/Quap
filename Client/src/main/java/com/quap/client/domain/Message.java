package com.quap.client.domain;

import java.util.Date;

public record Message(String content, Date timestamp, int senderID) implements Content {

    @Override
    public String toString() {
        return "Message[" +
                "content=" + content + ", " +
                "timestamp=" + timestamp + ", " +
                "senderID=" + senderID + ']';
    }

    public String display() {
        return timestamp + " " + senderID + ": " + content;
    }
}