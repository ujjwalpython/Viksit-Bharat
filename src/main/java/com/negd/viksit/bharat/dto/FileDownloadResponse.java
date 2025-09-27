package com.negd.viksit.bharat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileDownloadResponse {
    private byte[] fileBytes;
    private String fileName;
}

