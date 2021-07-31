package com.nyash.sneakychat.api.service;

import com.nyash.sneakychat.api.controller.ws.ParticipantWsController;
import com.nyash.sneakychat.api.domain.Participant;
import com.nyash.sneakychat.api.dto.ParticipantDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ParticipantService {

    SimpMessagingTemplate messagingTemplate;

    private static final Map<String, Participant> participantMap = new ConcurrentHashMap<>();

    SetOperations<String, Participant> setOperations;

    public void handleSubscription(String sessionId, String participantId, String chatId) {

        Participant participant = Participant.builder()
                .sessionId(sessionId)
                .id(participantId)
                .chatId(chatId)
                .build();

        participantMap.put(participant.getId(), participant);

        setOperations.add(ParticipantKeyHelper.makeKey(chatId), participant);

        messagingTemplate.convertAndSend(
                ParticipantWsController.getFetchParticipantJoinInChat(chatId),
                ParticipantDto.builder()
                        .id(participant.getId())
                        .enterAt(participant.getEnterAt())
                        .build()
        );
    }

    private static class ParticipantKeyHelper {

        private static final String KEY = "com:nyash:sneakychat:chats:{chat_id}:participants";

        public static String makeKey(String chatId) {
            return KEY.replace("{chat_id}", chatId);
        }
    }
}
