package com.github.luchbheag.livejournal_telegrambot.service;

import com.github.luchbheag.livejournal_telegrambot.repository.ConfirmationInfoRepository;
import com.github.luchbheag.livejournal_telegrambot.repository.entity.ConfirmationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfirmationInfoServiceImpl implements ConfirmationInfoService {
    private ConfirmationInfoRepository confirmationInfoRepository;

    @Autowired
    public ConfirmationInfoServiceImpl(ConfirmationInfoRepository confirmationInfoRepository) {
        this.confirmationInfoRepository = confirmationInfoRepository;
    }

    @Override
    public ConfirmationInfo save(String chatId, String blogName) {
        Optional<ConfirmationInfo> prevConfirmationInfo = confirmationInfoRepository.findById(chatId);
        prevConfirmationInfo.ifPresent(confirmationInfo -> confirmationInfoRepository.delete(confirmationInfo));
        ConfirmationInfo confirmationInfo = new ConfirmationInfo();
        confirmationInfo.setChatId(chatId);
        confirmationInfo.setBlogName(blogName);
        return confirmationInfoRepository.save(confirmationInfo);
    }

    @Override
    public Optional<ConfirmationInfo> findById(String chatId) {
        return confirmationInfoRepository.findById(chatId);
    }

    @Override
    public void delete(ConfirmationInfo confirmationInfo) {
        confirmationInfoRepository.delete(confirmationInfo);
    }

    @Override
    public void deleteById(String chatId) {
        confirmationInfoRepository.deleteById(chatId);
    }
}
