package xyz.nucte.msg;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Message implements MessageObject {
    private String type;
    private String content;

    public Message() {
    }

    public Message(String type, String content) {
        this.type = type;
        this.content = content;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
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

    public String toJSON() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    public static Message deserialize(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, Message.class);
    }
}


