package com.nyash.sneakychat.api.controller.rest;

import com.nyash.sneakychat.api.dto.ParticipantDto;
import com.nyash.sneakychat.api.factory.ParticipantDtoFactory;
import com.nyash.sneakychat.api.service.ParticipantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class ParticipantRestController {

    ParticipantDtoFactory participantDtoFactory;

    ParticipantService participantService;

    public static final String FETCH_PARTICIPANTS = "/api/chats/{chat_id}/participants";

    @GetMapping(FETCH_PARTICIPANTS)
    public List<ParticipantDto> fetchParticipants(@PathVariable("chat_id") String chatId) {

        return participantService
                .getParticipants(chatId)
                .map(participantDtoFactory::makeParticipantDto)
                .collect(Collectors.toList());
    }
}
