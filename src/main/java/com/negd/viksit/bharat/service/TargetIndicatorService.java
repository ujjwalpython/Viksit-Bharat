package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.model.master.TargetIndicator;
import com.negd.viksit.bharat.repository.TargetIndicatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TargetIndicatorService {
    private final TargetIndicatorRepository targetIndicatorRepository;

    public List<TargetIndicator> getAllTargetIndicators() {
        return targetIndicatorRepository.findAll();
    }
}
