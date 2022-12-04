package com.reesen.Reesen.service.interfaces;

import com.reesen.Reesen.model.Message;
import com.reesen.Reesen.model.User;

import java.util.List;

public interface IMessageService {
    Message save(Message message);
    List<Message> findBySenderAndReceiver(User sender, User receiver);

}
