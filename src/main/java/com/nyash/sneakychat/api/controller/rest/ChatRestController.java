package com.nyash.sneakychat.api.controller.rest;

import com.nyash.sneakychat.api.dto.ChatDto;
import com.nyash.sneakychat.api.factory.ParticipantDtoFactory;
import com.nyash.sneakychat.api.service.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class ChatRestController {

    ChatService chatService;

    ParticipantDtoFactory participantDtoFactory;

    public static final String FETCH_CHATS = "/api/chats";

    @GetMapping(FETCH_CHATS)
    public List<ChatDto> fetchChats() {

        return chatService
                .getChats()
                .map()
                .collect(Collectors.toList());
    }

}
