package com.negd.viksit.bharat.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.repository.MinistryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MinistryService {
    private final MinistryRepository ministryRepository;

//    public List<Ministry> getAllMinistries() {
//        return ministryRepository.findAll();
//    }
    
    public List<Ministry> getAllMinistries() {
        List<Ministry> ministries = ministryRepository.findAll();
        List<Ministry> result = new ArrayList<>();

        for (Ministry ministry : ministries) {
            if (ministry.getDepartments() != null && !ministry.getDepartments().isEmpty()) {
                for (var dept : ministry.getDepartments()) {
                    // Clone ministry and override name for each department
                    Ministry copy = Ministry.builder()
                            .id(ministry.getId())
                            .code(ministry.getCode())
                            .description(ministry.getDescription())
                            .isActive(ministry.getIsActive())
                            .isDeleted(ministry.getIsDeleted())
                            .name(ministry.getName() + " / " + dept.getName())
                            .build();
                    result.add(copy);
                }
            } else {
                result.add(ministry);
            }
        }

        return result;
    }
}

