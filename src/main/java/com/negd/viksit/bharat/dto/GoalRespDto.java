package com.negd.viksit.bharat.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class GoalRespDto {
    private UUID id;
    private String status;
}
