package com.negd.viksit.bharat.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectSchemeDto {
    private String name;
    private String type;
    private UUID ministryId;
    private String description;
    private LocalDate targetDate;
    private BigDecimal totalBudgetRequired;
    private Integer beneficiariesNo;
    private String status;
    private List<SchemeKeyDeliverableDto> keyDeliverables;
}


