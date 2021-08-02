package com.nyash.sneakychat.api.factory;

import com.nyash.sneakychat.api.domain.Participant;
import com.nyash.sneakychat.api.dto.ParticipantDto;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ParticipantDtoFactory {

    public ParticipantDto makeParticipantDto(Participant participant) {

        return ParticipantDto
                .builder()
                .id(participant.getId())
                .enterAt(Instant.ofEpochMilli(participant.getEnterAt().toEpochMilli()))
                .build();
    }
}
