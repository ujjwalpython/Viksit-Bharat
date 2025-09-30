package com.negd.viksit.bharat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectSchemeResponseDto {
    private String id;
    private String name;
    private String type;
    private String ministry;
    private String description;
    private LocalDate targetDate;
    private BigDecimal totalBudgetRequired;
    private Integer beneficiariesNo;
    private String status;
    @JsonFormat(pattern = "dd MMM yyyy, HH:mm")
    private LocalDateTime LastUpdate;
    private List<SchemeKeyDeliverableRespDto> keyDeliverables;
}

