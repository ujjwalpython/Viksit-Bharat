package com.negd.viksit.bharat.controller;

//import com.negd.viksit.bharat.model.Document;
//import com.negd.viksit.bharat.service.DocumentService;
//import com.negd.viksit.bharat.util.ResponseGenerator;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/v1/documents")
//@RequiredArgsConstructor
public class DocumentController {

//    private final DocumentService documentService;
//
//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadDocument(
//            @RequestParam("file") MultipartFile file,
//            HttpServletRequest request) throws Exception {
//
//        String savedDoc = documentService.uploadFile(file);
//        return ResponseGenerator.success(savedDoc, "File uploaded successfully", request);
//    }
//
///*
//    @GetMapping("/{id}/download")
//    public ResponseEntity<?> downloadDocument(@PathVariable UUID id) throws IOException {
//        byte[] fileBytes = documentService.downloadFile(id);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + id)
//                .body(fileBytes);
//    }
//*/
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteDocument(@PathVariable String fileUrl, HttpServletRequest request) {
//        documentService.deleteFile(fileUrl);
//        return ResponseGenerator.success(null, "File deleted successfully", request);
//    }
}
