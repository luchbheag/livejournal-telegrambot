package com.github.luchbheag.livejournal_telegrambot.service;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.ConfirmationInfo;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for manipulation with {@link ConfirmationInfo}
 */
@Service
public interface ConfirmationInfoService {
    ConfirmationInfo save(String chatId, String blogName);

    Optional<ConfirmationInfo> findById(String chatId);

    void delete(ConfirmationInfo confirmationInfo);

    void deleteById(String chatId);
}
