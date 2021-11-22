package com.quap.client.domain;

public record Message(String content, String timestamp, int senderID) {
        @Override
        public String toString() {
            return content;
        }
}