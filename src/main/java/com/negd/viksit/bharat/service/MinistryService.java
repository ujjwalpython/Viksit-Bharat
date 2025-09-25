package com.negd.viksit.bharat.service;

import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.repository.MinistryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MinistryService {
    private final MinistryRepository ministryRepository;

    public List<Ministry> getAllMinistries() {
        return ministryRepository.findAll();
    }
}

