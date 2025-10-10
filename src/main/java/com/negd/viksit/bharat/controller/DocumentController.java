package com.negd.viksit.bharat.controller;

import com.negd.viksit.bharat.dto.DocumentDto;
import com.negd.viksit.bharat.dto.FileDownloadResponse;
import com.negd.viksit.bharat.model.Document;
import com.negd.viksit.bharat.service.DocumentService;
import com.negd.viksit.bharat.util.ResponseGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file,@RequestParam(required = false) String refType,@RequestParam(required = false) String refId,
            HttpServletRequest request) throws Exception {

        DocumentDto savedDoc = documentService.uploadFile(file,refType,refId);
        return ResponseGenerator.success(savedDoc, "File uploaded successfully", request);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<?> downloadDocument(@PathVariable UUID id) throws IOException {
        FileDownloadResponse fileBytes = documentService.downloadFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileBytes.getFileName() + "\"")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition, Content-Length, Content-Type")
                .contentLength(fileBytes.getFileBytes().length)
                .body(fileBytes.getFileBytes());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable UUID id, HttpServletRequest request) {
        documentService.deleteFile(id);
        return ResponseGenerator.success(null, "File deleted successfully", request);
    }
}
