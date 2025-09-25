package com.negd.viksit.bharat.controller;

import com.negd.viksit.bharat.model.master.TargetIndicator;
import com.negd.viksit.bharat.service.TargetIndicatorService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/indicators")
public class IndicatorController {
    private final TargetIndicatorService indicatorService;

    public IndicatorController(TargetIndicatorService indicatorService) {
        this.indicatorService = indicatorService;
    }

    @GetMapping
    public List<TargetIndicator> getAllIndicators() {
        return indicatorService.getAllTargetIndicators();
    }
}

