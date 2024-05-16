package xyz.nucte.msg;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Message implements MessageObject {
    private MessageType type;
    private String content;

    public Message() {
    }

    public Message(MessageType type, String content) {
        this.type = type;
        this.content = content;
    }

    @Override
    public MessageType getType() {
        return type;
    }

    @Override
    public void setType(MessageType type) {
        this.type = type;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    public String toJSON() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    public static Message deserialize(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, Message.class);
    }
}


