package com.nyash.sneakychat.api.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ParticipantService {

    SimpMessagingTemplate messagingTemplate;

    public void handleSubscription(String sessionId, String participantId, String chatId) {

        messagingTemplate.convertAndSend();

    }
}
