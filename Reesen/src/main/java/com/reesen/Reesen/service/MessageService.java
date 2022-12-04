package com.reesen.Reesen.service;

import com.reesen.Reesen.model.Message;
import com.reesen.Reesen.model.User;
import com.reesen.Reesen.repository.MessageRepository;
import com.reesen.Reesen.service.interfaces.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService implements IMessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message save(Message message) {
        return this.messageRepository.save(message);
    }

    @Override
    public List<Message> findBySenderAndReceiver(User sender, User receiver) {
        return this.messageRepository.findMessagesBySenderAndReceiver(sender, receiver);
    }
}
