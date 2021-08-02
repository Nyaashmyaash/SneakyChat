package com.nyash.sneakychat.api.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Participant implements Serializable {

    @Builder.Default
    Long enterAt = Instant.now().toEpochMilli();

    String sessionId;

    String id;

    String chatId;
}
