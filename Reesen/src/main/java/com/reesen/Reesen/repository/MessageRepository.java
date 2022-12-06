package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Message;
import com.reesen.Reesen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Set<Message> getMessagesBySender(User sender);
}
