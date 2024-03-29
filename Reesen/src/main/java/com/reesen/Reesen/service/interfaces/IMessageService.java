package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Message;
import com.reesen.Reesen.model.User;

import java.util.Set;

public interface IMessageService {
    Message save(Message message);
    Set<Message> getMessagesBySender(User sender);

    Set<Message> getAll(User user);
}
