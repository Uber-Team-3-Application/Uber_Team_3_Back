package com.reesen.Reesen.dto;

import java.util.List;

public class MessagesDTO {
    int size;
    List<MessageDTO> messages;

    public MessagesDTO(int size, List<MessageDTO> messages) {
        this.size = size;
        this.messages = messages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }
}
