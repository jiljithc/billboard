package com.jeeniv.billboard.dto;

import com.jeeniv.billboard.model.Billboard;
import com.jeeniv.billboard.model.BillboardType;
import com.jeeniv.billboard.model.ExpirationStatus;

import java.time.LocalDate;

public record BillboardResponse(
    Long id,
    String code,
    String title,
    String area,
    String address,
    Double latitude,
    Double longitude,
    BillboardType type,
    String dimensions,
    Double monthlyRate,
    Integer dailyImpressions,
    String currentAdvertiser,
    String campaignName,
    LocalDate startDate,
    LocalDate expiryDate,
    Boolean isVacant,
    ExpirationStatus status,
    Long daysRemaining
) {
    public static BillboardResponse fromEntity(Billboard b) {
        return new BillboardResponse(
            b.getId(),
            b.getCode(),
            b.getTitle(),
            b.getArea(),
            b.getAddress(),
            b.getLatitude(),
            b.getLongitude(),
            b.getType(),
            b.getDimensions(),
            b.getMonthlyRate(),
            b.getDailyImpressions(),
            b.getCurrentAdvertiser(),
            b.getCampaignName(),
            b.getStartDate(),
            b.getExpiryDate(),
            b.getIsVacant(),
            b.computeStatus(),
            b.getDaysRemaining()
        );
    }
}