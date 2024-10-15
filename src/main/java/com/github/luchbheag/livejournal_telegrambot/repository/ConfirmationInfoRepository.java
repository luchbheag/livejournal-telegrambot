package com.github.luchbheag.livejournal_telegrambot.repository;

import com.github.luchbheag.livejournal_telegrambot.repository.entity.ConfirmationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link Repository} for {@link ConfirmationInfo} entity.
 */
@Repository
public interface ConfirmationInfoRepository extends JpaRepository<ConfirmationInfo, String> {
}
