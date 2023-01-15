package com.reesen.Reesen.repository;

import com.reesen.Reesen.model.Message;
import com.reesen.Reesen.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Set<Message> getMessagesBySender(User sender);

    @Query("select m from Message m where m.sender=:user or m.receiver=:user " +
            "order by m.timeOfSend asc")
    Set<Message> findAll(User user);
}
