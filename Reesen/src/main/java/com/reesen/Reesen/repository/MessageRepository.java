package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Message;
import com.reesen.Reesen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findMessagesBySenderAndReceiver(User sender, User receiver);
}
