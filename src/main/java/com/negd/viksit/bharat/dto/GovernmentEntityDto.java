package com.negd.viksit.bharat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GovernmentEntityDto {

    private UUID id;
    private String code;
    private String name;
    private String description;
    private String type;
    private Boolean isActive;
    private Boolean isDeleted;
    private ParentDto parent;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ParentDto {
        private UUID id;
        private String name;
    }
}