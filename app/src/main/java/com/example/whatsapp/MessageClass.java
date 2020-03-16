package com.example.whatsapp;

public class MessageClass {
    String message;
    String date;
    String time;
    String type;
    String from;
    String to;
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public MessageClass(String message,String key, String date, String time, String type, String from, String to) {
        this.message = message;
        this.date = date;
        this.time = time;
        this.type = type;
        this.from = from;
        this.to = to;
        this.key = key;
    }

    public MessageClass() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
