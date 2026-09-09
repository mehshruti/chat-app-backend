package com.chatApplication.chat.controller;

import org.springframework.web.bind.annotation.*;

import com.chatApplication.chat.entity.Message;
import com.chatApplication.chat.entity.User;
import com.chatApplication.chat.repo.MessageRepository;
import com.chatApplication.chat.repo.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class MessageController {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageController(
            MessageRepository messageRepository,
            UserRepository userRepository) {

        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> getUsers() {

        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUser(
            @PathVariable Long id) {

        return userRepository
                .findById(id)
                .orElseThrow();
    }


    @PostMapping("/messages/send")
    public Message sendMessage(
            @RequestBody Message message) {

        return messageRepository.save(message);
    }


    @GetMapping("/messages/{user1}/{user2}")
    public List<Message> getChat(
            @PathVariable Long user1,
            @PathVariable Long user2) {

        return messageRepository
                .findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
                        user1,
                        user2,
                        user2,
                        user1
                );
    }
}