package com.jeeniv.billboard.service;


import com.jeeniv.billboard.dto.BillboardRequest;
import com.jeeniv.billboard.dto.BillboardResponse;
import com.jeeniv.billboard.dto.RenewalRequest;
import com.jeeniv.billboard.model.Billboard;
import com.jeeniv.billboard.repository.BillboardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillboardService {

    private final BillboardRepository repository;

    public BillboardService(BillboardRepository repository) {
        this.repository = repository;
    }

    public List<BillboardResponse> getAllBillboards(String area) {
        List<Billboard> boards = (area != null && !area.isBlank() && !area.equalsIgnoreCase("all"))
                ? repository.findByAreaIgnoreCase(area.trim())
                : repository.findAll();

        return boards.stream()
                .map(BillboardResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public BillboardResponse getById(Long id) {
        return repository.findById(id)
                .map(BillboardResponse::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Billboard not found with ID: " + id));
    }

    @Transactional
    public BillboardResponse create(BillboardRequest req) {
        boolean isVacant = req.isVacant() != null ? req.isVacant() : (req.expiryDate() == null);

        Billboard b = new Billboard(
                req.code().trim().toUpperCase(),
                req.title().trim(),
                req.area().trim(),
                req.address() != null ? req.address().trim() : "",
                req.latitude(),
                req.longitude(),
                req.type(),
                req.dimensions() != null && !req.dimensions().isBlank() ? req.dimensions().trim() : "Standard",
                req.monthlyRate() != null ? req.monthlyRate() : 0.0,
                req.dailyImpressions() != null ? req.dailyImpressions() : 0,
                isVacant ? null : req.currentAdvertiser(),
                isVacant ? null : req.campaignName(),
                isVacant ? null : req.startDate(),
                isVacant ? null : req.expiryDate(),
                isVacant
        );
        return BillboardResponse.fromEntity(repository.save(b));
    }

    @Transactional
    public BillboardResponse renew(Long id, RenewalRequest req) {
        Billboard board = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Billboard not found: " + id));

        LocalDate baseDate = (board.getExpiryDate() != null && board.getExpiryDate().isAfter(LocalDate.now()))
                ? board.getExpiryDate()
                : LocalDate.now();

        board.setExpiryDate(baseDate.plusMonths(req.additionalMonths()));
        board.setIsVacant(false);

        if (req.advertiser() != null && !req.advertiser().isBlank()) {
            board.setCurrentAdvertiser(req.advertiser().trim());
        }
        if (req.campaignName() != null && !req.campaignName().isBlank()) {
            board.setCampaignName(req.campaignName().trim());
        }

        return BillboardResponse.fromEntity(repository.save(board));
    }

    public List<String> getAllAreas() {
        return repository.findDistinctAreas();
    }
}