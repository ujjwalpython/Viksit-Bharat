package com.negd.viksit.bharat.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class GoalRespDto {
    private String id;
    private String status;
}
