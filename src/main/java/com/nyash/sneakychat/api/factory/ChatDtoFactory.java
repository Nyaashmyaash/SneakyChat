package com.nyash.sneakychat.api.factory;

import com.nyash.sneakychat.api.domain.Chat;
import com.nyash.sneakychat.api.dto.ChatDto;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ChatDtoFactory {

    public ChatDto makeChatDto(Chat chat) {

        return ChatDto.builder()
                .id(chat.getId())
                .name(chat.getName())
                .createdAt(Instant.ofEpochMilli(chat.getCreatedAt().toEpochMilli()))
                .build();
    }
}
