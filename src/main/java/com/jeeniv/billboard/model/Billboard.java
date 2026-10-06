package com.jeeniv.billboard.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "billboards")
public class Billboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // e.g. "BLB-101"

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String area;

    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillboardType type;

    private String dimensions; // e.g., "40x20 ft"
    private Double monthlyRate;
    private Integer dailyImpressions;

    private String currentAdvertiser;
    private String campaignName;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private Boolean isVacant = false;

    public Billboard() {}

    public Billboard(String code, String title, String area, String address, Double latitude, Double longitude,
                     BillboardType type, String dimensions, Double monthlyRate, Integer dailyImpressions,
                     String currentAdvertiser, String campaignName, LocalDate startDate, LocalDate expiryDate, Boolean isVacant) {
        this.code = code;
        this.title = title;
        this.area = area;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.dimensions = dimensions;
        this.monthlyRate = monthlyRate;
        this.dailyImpressions = dailyImpressions;
        this.currentAdvertiser = currentAdvertiser;
        this.campaignName = campaignName;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.isVacant = isVacant;
    }

    public ExpirationStatus computeStatus() {
        if (Boolean.TRUE.equals(this.isVacant) || this.expiryDate == null) {
            return ExpirationStatus.VACANT;
        }
        long days = ChronoUnit.DAYS.between(LocalDate.now(), this.expiryDate);
        if (days < 0) {
            return ExpirationStatus.EXPIRED;
        } else if (days <= 14) {
            return ExpirationStatus.EXPIRING_SOON;
        } else if (days <= 30) {
            return ExpirationStatus.PENDING_RENEWAL;
        } else {
            return ExpirationStatus.ACTIVE;
        }
    }

    public Long getDaysRemaining() {
        if (Boolean.TRUE.equals(this.isVacant) || this.expiryDate == null) {
            return null;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), this.expiryDate);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public BillboardType getType() { return type; }
    public void setType(BillboardType type) { this.type = type; }
    public String getDimensions() { return dimensions; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }
    public Double getMonthlyRate() { return monthlyRate; }
    public void setMonthlyRate(Double monthlyRate) { this.monthlyRate = monthlyRate; }
    public Integer getDailyImpressions() { return dailyImpressions; }
    public void setDailyImpressions(Integer dailyImpressions) { this.dailyImpressions = dailyImpressions; }
    public String getCurrentAdvertiser() { return currentAdvertiser; }
    public void setCurrentAdvertiser(String currentAdvertiser) { this.currentAdvertiser = currentAdvertiser; }
    public String getCampaignName() { return campaignName; }
    public void setCampaignName(String campaignName) { this.campaignName = campaignName; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public Boolean getIsVacant() { return isVacant; }
    public void setIsVacant(Boolean vacant) { isVacant = vacant; }
}