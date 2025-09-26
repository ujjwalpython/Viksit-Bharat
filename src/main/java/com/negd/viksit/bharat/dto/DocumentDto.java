package com.negd.viksit.bharat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDto {
    private UUID id;
    private String fileName;
    private String fileType;
    private String fileUrl;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long size;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String referenceType;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long referenceId;
}

