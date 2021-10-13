package com.quap.cryptographic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private final String time;
    private final String prefix;
    private final String suffix;
    private final String userName;
    private final String address;
    private final String message;

   // private final String hashCode;
    private String key;


    public Message(String prefix, String userName, String message, String address, String suffix) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd|HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        this.userName = userName;
        time = dtf.format(now);
        this.message = message;
        this.prefix = prefix;
        this.suffix = suffix;

        //this.hashCode = generateHashCode(this.prefix, this.message, time, this.suffix);
        this.address = address;
    }

    /*
    private String generateHashCode(String prefix, String message, String time, String suffix) {
        String hashCode;
        String key;
        setKey("");
        return "abcd";
    }

    private void setKey(String key) {
        this.key = key;
    }

    public String getHashCode() {
        return hashCode;
    } */

    @Override
    public String toString() {
        return prefix + "/u/" + userName + "/u/" + "<" + message + ">" + "/a/" + address + "/a/" + "/t/" + time + "/t/" + suffix;
    }

}
