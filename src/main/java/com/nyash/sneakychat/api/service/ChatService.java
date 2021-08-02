package com.nyash.sneakychat.api.service;

import com.nyash.sneakychat.api.controller.ws.ChatWsController;
import com.nyash.sneakychat.api.domain.Chat;
import com.nyash.sneakychat.api.dto.ChatDto;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ChatService {

    SimpMessagingTemplate messagingTemplate;

    SetOperations<String, Chat> setOperations;

    private static final String KEY = "com:nyash:sneakychat:chats";

    public void createChat(String chatName) {

        Chat chat = Chat.builder()
                .name(chatName)
                .build();

        setOperations.add(KEY, chat);


        messagingTemplate.convertAndSend(
                ChatWsController.FETCH_CREATE_CHAT_EVENT,
                ChatDto.builder()
                        .id(chat.getId())
                        .name(chat.getName())
                        .createdAt(chat.getCreatedAt())
                        .build()
        );
    }

    public void deleteChat(String chatId) {

        getChats()
                .filter(chat -> Objects.equals(chatId, chat.getId()))
                .findAny()
                .ifPresent(chat -> {

                    setOperations.add(KEY, chat);

                    messagingTemplate.convertAndSend(
                            ChatWsController.FETCH_DELETE_CHAT_EVENT,
                            ChatDto.builder()
                                    .id(chat.getId())
                                    .name(chat.getName())
                                    .createdAt(chat.getCreatedAt())
                                    .build()
                    );


                });

        setOperations.add(KEY, chat)
    }

    public Stream<Chat> getChats() {
        return Optional
                .ofNullable(setOperations.members(KEY))
                .orElseGet(HashSet::new)
                .stream();
    }
}
