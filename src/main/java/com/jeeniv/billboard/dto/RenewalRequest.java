package com.jeeniv.billboard.dto;
import jakarta.validation.constraints.NotNull;
public record RenewalRequest(
    @NotNull Integer additionalMonths,
    String advertiser,
    String campaignName
) {}