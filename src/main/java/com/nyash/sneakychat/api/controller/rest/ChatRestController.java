package com.nyash.sneakychat.api.controller.rest;

import com.nyash.sneakychat.api.dto.ChatDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class ChatRestController {



    public static final String FETCH_CHATS = "/api/chats";

    @GetMapping(FETCH_CHATS)
    public List<ChatDto> fetchChats() {

    }

}
