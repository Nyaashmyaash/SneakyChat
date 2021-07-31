package com.nyash.sneakychat.api.controller.ws;

import com.nyash.sneakychat.api.domain.Chat;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChatWsController {

    public static final String CREATE_CHAT = "/topic.chat.{chat_name}.create";
    public static final String CREATE_CHAT_EVENT = "/topic.chat.create.event";

    @MessageMapping(CREATE_CHAT)
    public void createChat(@DestinationVariable("chat_name") String chatName) {

        Chat chat = Chat.builder()
                .name(chatName)
                .build();
    }

    @SubscribeMapping(CREATE_CHAT_EVENT)
    public void

}
