package com.nyash.sneakychat.api.ws;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Log4j2
@ActiveProfiles("test-nyash")
@RunWith(JUnitPlatform.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WebSocketTests {

    @Value("${local.server.port}")
    private int port;

    private static WebClient firstClient;

    @BeforeAll
    public void setup() throws Exception {

        RunStompFrameHandler runStompFrameHandler = new RunStompFrameHandler(new CompletableFuture<>());

        String wsUrl = "ws://127.0.0.1:" + port + "/ws";

        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
    }

    private List<Transport> createTransportClient() {

    }

    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class  WebClient {

        WebSocketStompClient stompClient;

        StompSession stompSession;

        String sessionToken;

        RunStompFrameHandler handler;
    }

    @Data
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class RunStompFrameHandler implements StompFrameHandler {

        CompletableFuture<Object> future;

        @Override
        public @NonNull Type getPayloadType(StompHeaders stompHeaders) {

            log.info(stompHeaders.toString());

            return byte[].class;
        }

        @Override
        public void handleFrame(@NonNull StompHeaders headers, Object o) {

            log.info(o);

            future.complete(o);

            future = new CompletableFuture<>();
        }
    }
}
