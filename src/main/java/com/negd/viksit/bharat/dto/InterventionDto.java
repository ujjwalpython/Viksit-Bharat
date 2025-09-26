package com.negd.viksit.bharat.dto;
import lombok.Data;

import java.util.UUID;

@Data
public class InterventionDto {
    private UUID targetDescriptionId;
    private String targetDescription;

    private String presentValue;
    private String presentUnit;
    private Integer presentYear;

    private String target2030Value;
    private String target2030Unit;

    private String target2047Value;
    private String target2047Unit;

    private Integer sortOrder;
}


