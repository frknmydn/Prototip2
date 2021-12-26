package com.furkanmeydan.prototip2.Models;

public class Message {
    public String sender;
    public String receiver;
    Long timestamp;
    String message;
    String report;

    public Message(String sender, String receiver, Long timestamp, String message, String report) {
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.message = message;
        this.report = report;
    }

    public Message(String sender, String receiver, Long timestamp, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Message() {
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
