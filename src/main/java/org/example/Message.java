package org.example;

public class Message {
    private String type; // "JOIN" ou "CHAT"
    private String nick;
    private String content;
    private String timestamp;

    public Message() {}

    public Message(String type, String nick, String content, String timestamp) {
        this.type = type;
        this.nick = nick;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getType() { return type; }
    public String getNick() { return nick; }
    public String getContent() { return content; }
    public String getTimestamp() { return timestamp; }
}