package com.nyash.sneakychat.api.controller.ws;

import com.nyash.sneakychat.api.domain.Chat;
import com.nyash.sneakychat.api.dto.ChatDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
public class ChatWsController {

    SimpMessagingTemplate messagingTemplate;

    public static final String CREATE_CHAT = "/topic.chat.{chat_name}.create";
    public static final String FETCH_CREATE_CHAT_EVENT = "/topic.chat.create.event";

    @MessageMapping(CREATE_CHAT)
    public void createChat(@DestinationVariable("chat_name") String chatName) {

        Chat chat = Chat.builder()
                .name(chatName)
                .build();

        //TODO: save in redis

        messagingTemplate.convertAndSend(
                FETCH_CREATE_CHAT_EVENT,
                ChatDto.builder()
                        .id(chat.getId())
        );
    }

    @SubscribeMapping(FETCH_CREATE_CHAT_EVENT)
    public ChatDto fetchCreateChatEvent() {
        return null;
    }

}
