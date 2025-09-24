package com.negd.viksit.bharat.dto;
import lombok.Data;

import java.util.UUID;

@Data
public class InterventionDto {
    private UUID targetDescriptionId;
    private String targetDescription;
    private Double presentValue;
    private Integer presentYear;
    private Double target2030Value;
    private Double target2047Value;
    private Integer sortOrder;

}

