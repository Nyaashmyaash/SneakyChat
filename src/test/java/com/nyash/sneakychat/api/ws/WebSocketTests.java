package com.nyash.sneakychat.api.ws;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Log4j2
@ActiveProfiles("test-nyash")
@RunWith(JUnitPlatform.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebSocketTests {

    @Value("${local.server.port}")
    private int port;

    private static WebClient firstClient;

    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class  WebClient {

        WebSocketStompClient stompClient;

        StompSession stompSession;

        String sessionToken;

        RunStompFrameHandler handler;
    }

    private static class RunStompFrameHandler{

    }
}
