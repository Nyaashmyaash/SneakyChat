package com.nyash.sneakychat.api.service;

import com.nyash.sneakychat.api.domain.Chat;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    SetOperations<String, Chat> setOperations;
}
