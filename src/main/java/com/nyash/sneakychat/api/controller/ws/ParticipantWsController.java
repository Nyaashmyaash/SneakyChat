package com.nyash.sneakychat.api.controller.ws;

import com.nyash.sneakychat.api.dto.MessageDto;
import com.nyash.sneakychat.api.dto.ParticipantDto;
import com.nyash.sneakychat.api.service.ParticipantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
public class ParticipantWsController {

    public static final String FETCH_PARTICIPANT_JOIN_IN_CHAT = "/topic/chats.{chat_id}.participants.join";
    public static final String FETCH_PARTICIPANT_LEAVE_CHAT = "/topic/chats.{chat_id}.participants.leave";

    @SubscribeMapping(FETCH_PARTICIPANT_JOIN_IN_CHAT)
    public ParticipantDto fetchParticipantJoinInChat() {
        return null;
    }

    @SubscribeMapping(FETCH_PARTICIPANT_LEAVE_CHAT)
    public ParticipantDto fetchParticipantLeaveChat() {
        return null;
    }
}
