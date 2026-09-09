package com.chatApplication.chat.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatApplication.chat.entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
            Long senderId1, Long receiverId1,
            Long senderId2, Long receiverId2
    );
}
