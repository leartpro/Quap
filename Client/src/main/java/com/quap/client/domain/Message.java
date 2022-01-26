package com.quap.client.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public record Message(String content, Date timestamp, int senderID, String senderName) implements Content {

    @Override
    public String toString() {
        return "Message[" +
                "content=" + content + ", " +
                "timestamp=" + timestamp + ", " +
                "senderID=" + senderID + ", " +
                "senderName=" + senderName + ']';

    }

    private static String formatDate(Date date) {
        SimpleDateFormat fromat = new SimpleDateFormat("EEE, MMM d, y h:mm a");
        String dateString = fromat.format(date);
        return dateString;
    }

    @Override
    public String display() {
        return timestamp + " " + senderName + "#" + senderID + ": " + content;
    }

    @Override
    public String content() {
        return formatDate(timestamp) + " " + senderName + ": " + content;
    }
}