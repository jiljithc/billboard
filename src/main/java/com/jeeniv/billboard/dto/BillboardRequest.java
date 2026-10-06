package com.jeeniv.billboard.dto;
import com.jeeniv.billboard.model.BillboardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record BillboardRequest(
    @NotBlank(message = "Board code is required") 
    String code,

    @NotBlank(message = "Title is required") 
    String title,

    @NotBlank(message = "Area is required") 
    String area,

    String address,

    @NotNull(message = "Latitude is required") 
    Double latitude,

    @NotNull(message = "Longitude is required") 
    Double longitude,

    @NotNull(message = "Billboard type is required") 
    BillboardType type,

    String dimensions,
    Double monthlyRate,
    Integer dailyImpressions,
    String currentAdvertiser,
    String campaignName,
    LocalDate startDate,
    LocalDate expiryDate,
    Boolean isVacant
) {}