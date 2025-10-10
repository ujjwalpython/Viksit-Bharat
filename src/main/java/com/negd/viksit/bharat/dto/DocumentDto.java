package com.negd.viksit.bharat.dto;

import com.negd.viksit.bharat.model.Document;
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
    private Long size;


    public DocumentDto(Document document) {
        id = document.getId();
        fileName = document.getFileName();
        fileType = document.getFileType();
        ;
        fileUrl = document.getFileUrl();
        size = document.getSize();
    }
}

