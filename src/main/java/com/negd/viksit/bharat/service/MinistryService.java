package com.negd.viksit.bharat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.negd.viksit.bharat.dto.GovernmentEntityDto;
import com.negd.viksit.bharat.model.master.GovernmentEntity;
import com.negd.viksit.bharat.repository.GovernmentEntityRepository;
import org.springframework.stereotype.Service;

import com.negd.viksit.bharat.model.master.Ministry;
import com.negd.viksit.bharat.repository.MinistryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MinistryService {
    private final MinistryRepository ministryRepository;
    private final GovernmentEntityRepository governmentEntityRepository;

//    public List<Ministry> getAllMinistries() {
//        return ministryRepository.findAll();
//    }
    
/*
    public List<Ministry> getAllMinistries() {
        List<Ministry> ministries = ministryRepository.findAll();
        List<Ministry> result = new ArrayList<>();

        for (Ministry ministry : ministries) {
            if (ministry.getDepartments() != null && !ministry.getDepartments().isEmpty()) {
                for (var dept : ministry.getDepartments()) {
                    // Clone ministry and override name for each department
                    Ministry copy = Ministry.builder()
                            .id(dept.getId())
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
*/
public List<GovernmentEntityDto> getAllMinistries() {
    List<GovernmentEntity> allEntities = governmentEntityRepository.findAll();

    List<GovernmentEntity> filteredEntities = allEntities.stream()
            .filter(e -> "DEPARTMENT".equals(e.getType())
                    || ("MINISTRY".equals(e.getType()) &&
                    allEntities.stream().noneMatch(child -> e.equals(child.getParent()))))
            .collect(Collectors.toList());

    return filteredEntities.stream()
            .map(this::toResponseDto)
            .collect(Collectors.toList());
}

    private GovernmentEntityDto toResponseDto(GovernmentEntity entity) {
        return GovernmentEntityDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .type(entity.getType())
                .isActive(entity.getIsActive())
                .isDeleted(entity.getIsDeleted())
                .parent(entity.getParent() != null
                        ? GovernmentEntityDto.ParentDto.builder()
                        .id(entity.getParent().getId())
                        .name(entity.getParent().getName())
                        .build()
                        : null)
                .build();
    }
}

