package com.jeeniv.billboard.controller;
import com.jeeniv.billboard.dto.BillboardRequest;
import com.jeeniv.billboard.dto.BillboardResponse;
import com.jeeniv.billboard.dto.RenewalRequest;
import com.jeeniv.billboard.service.BillboardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billboards")
@CrossOrigin(origins = "*")
public class BillboardRestController {

    private final BillboardService service;

    public BillboardRestController(BillboardService service) {
        this.service = service;
    }

    @GetMapping
    public List<BillboardResponse> list(@RequestParam(required = false) String area) {
        return service.getAllBillboards(area);
    }

    @GetMapping("/{id}")
    public BillboardResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<BillboardResponse> create(@Valid @RequestBody BillboardRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/renew")
    public BillboardResponse renew(@PathVariable Long id, @Valid @RequestBody RenewalRequest req) {
        return service.renew(id, req);
    }

    @GetMapping("/areas")
    public List<String> getAreas() {
        return service.getAllAreas();
    }
}