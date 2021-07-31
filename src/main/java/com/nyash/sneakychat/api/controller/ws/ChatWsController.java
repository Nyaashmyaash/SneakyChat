package com.nyash.sneakychat.api.controller.ws;

import com.nyash.sneakychat.api.domain.Chat;
import com.nyash.sneakychat.api.dto.ChatDto;
import com.nyash.sneakychat.api.dto.MessageDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
public class ChatWsController {

    SimpMessagingTemplate messagingTemplate;

    public static final String CREATE_CHAT = "/topic/chats.create";

    public static final String FETCH_CREATE_CHAT_EVENT = "/topic/chats.create.event";

    public static final String SEND_MESSAGE_TO_ALL = "/topic/chats.{chat_id}.messages.send";
    public static final String SEND_MESSAGE_TO_PARTICIPANT = "/topic/chats.{chat_id}.participants.{participant_id}.messages.send";

    public static final String FETCH_MESSAGES = "/topic/chats.{chat_id}.messages";
    public static final String FETCH_PERSONAL_MESSAGES = "/topic/chats.{chat_id}.participants.{participant_id}.messages";

    @MessageMapping(CREATE_CHAT)
    public void createChat(String chatName) {

        Chat chat = Chat.builder()
                .name(chatName)
                .build();

        //TODO: save in redis

        messagingTemplate.convertAndSend(
                FETCH_CREATE_CHAT_EVENT,
                ChatDto.builder()
                        .id(chat.getId())
                        .name(chat.getName())
                        .createdAt(chat.getCreatedAt())
                        .build()
        );
    }

    @SubscribeMapping(FETCH_CREATE_CHAT_EVENT)
    public ChatDto fetchCreateChatEvent() {
        return null;
    }

    @MessageMapping(SEND_MESSAGE_TO_ALL)
    public void sendMessageToAll(
            @DestinationVariable("chat_id") String chatId,
            String message,
            @Header String simpSessionId) {

        sendMessage(
                getFetchMessagesDestination(chatId),
                simpSessionId,
                message
        );
    }

    @MessageMapping(SEND_MESSAGE_TO_PARTICIPANT)
    public void SendMessageToParticipant(
            @DestinationVariable("chat_id") String chatId,
            @DestinationVariable("participant_id") String participantId,
            String message,
            @Header String simpSessionId) {

        sendMessage(
                getFetchPersonalMessagesDestination(chatId, participantId),
                simpSessionId,
                message
        );
    }

    @SubscribeMapping(FETCH_MESSAGES)
    public MessageDto fetchMessages() {
        return null;
    }

    @SubscribeMapping(FETCH_PERSONAL_MESSAGES)
    public MessageDto fetchPersonalMessages() {
        return null;
    }

    private void sendMessage(String destination, String sessionId, String message) {

        messagingTemplate.convertAndSend(
                destination,
                MessageDto.builder()
                        .from(sessionId)
                        .message(message)
                        .build()
        );
    }

    private String getFetchMessagesDestination(String chatId) {
        return FETCH_MESSAGES.replace("{chat_id}", chatId);
    }

    private String getFetchPersonalMessagesDestination(String chatId, String participantId) {
        return FETCH_PERSONAL_MESSAGES
                .replace("{chat_id}", chatId)
                .replace("{participant_id}", participantId);
    }
}
