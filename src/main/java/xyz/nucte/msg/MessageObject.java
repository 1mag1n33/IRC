package xyz.nucte.msg;

public interface MessageObject {
    MessageType getType();
    void setType(MessageType type);
    String getContent();
    void setContent(String content);
}


