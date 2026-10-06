package com.jeeniv.billboard.model;

public enum ExpirationStatus {
    EXPIRED,
    EXPIRING_SOON,    // <= 14 days
    PENDING_RENEWAL,  // 15 - 30 days
    ACTIVE,           // > 30 days
    VACANT
}