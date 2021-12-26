package com.quap.client.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public record Message(String content, Date timestamp, int senderID) implements Content {

    @Override
    public String toString() {
        return "Message[" +
                "content=" + content + ", " +
                "timestamp=" + timestamp + ", " +
                "senderID=" + senderID + ']';
    }

    private static Date formatDate(Date date) {
        String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String display() {
        return timestamp + " " + senderID + ": " + content;
    }

    @Override
    public String content() {
        return formatDate(timestamp) + " " + senderID + ": " + content;
    }
}