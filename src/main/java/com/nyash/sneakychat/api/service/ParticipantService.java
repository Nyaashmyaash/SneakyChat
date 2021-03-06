package com.nyash.sneakychat.api.service;

import com.nyash.sneakychat.api.controller.ws.ParticipantWsController;
import com.nyash.sneakychat.api.domain.Participant;
import com.nyash.sneakychat.api.dto.ParticipantDto;
import com.nyash.sneakychat.api.factory.ParticipantDtoFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ParticipantService {

    SimpMessagingTemplate messagingTemplate;

    ParticipantDtoFactory participantDtoFactory;

    ChatService chatService;

    private static final Map<String, Participant> sessionIdToParticipantMap = new ConcurrentHashMap<>();

    SetOperations<String, Participant> setOperations;

    public void handleJoinChat(String sessionId, String participantId, String chatId) {

        Participant participant = Participant.builder()
                .sessionId(sessionId)
                .id(participantId)
                .chatId(chatId)
                .build();

        sessionIdToParticipantMap.put(participant.getId(), participant);

        setOperations.add(ParticipantKeyHelper.makeKey(chatId), participant);

        messagingTemplate.convertAndSend(
                ParticipantWsController.getFetchParticipantJoinInChat(chatId),
                participantDtoFactory.makeParticipantDto(participant)
        );
    }

    @EventListener
    public void handleUnsubscribe(SessionUnsubscribeEvent event) {
        handleLeaveChat(event);
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        handleLeaveChat(event);
    }

    private void handleLeaveChat(AbstractSubProtocolEvent event) {

        final SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());

        Optional
                .ofNullable(headerAccessor.getSessionId())
                .map(sessionIdToParticipantMap::remove)
                .ifPresent(participant -> {

                    String chatId = participant.getChatId();

                    log.info(
                            String.format(
                                    "Participant leave from  \"%s\" chat.",
                                    participant.getSessionId(),
                                    chatId
                            )
                    );

                    String key = ParticipantKeyHelper.makeKey(chatId);

                    setOperations.remove(key, participant);

                    Optional
                            .ofNullable(setOperations.size(key))
                            .filter(size -> size == 0L)
                            .ifPresent(size -> chatService.deleteChat(chatId));

                    messagingTemplate.convertAndSend(
                            key,
                            participantDtoFactory.makeParticipantDto(participant)
                    );
                });

    }

    public Stream<Participant> getParticipants(String chatId) {
        return Optional
                .ofNullable(setOperations.members(ParticipantKeyHelper.makeKey(chatId)))
                .orElseGet(HashSet::new)
                .stream();
    }

    private static class ParticipantKeyHelper {

        private static final String KEY = "com:nyash:sneakychat:chats:{chat_id}:participants";

        public static String makeKey(String chatId) {
            return KEY.replace("{chat_id}", chatId);
        }
    }
}
