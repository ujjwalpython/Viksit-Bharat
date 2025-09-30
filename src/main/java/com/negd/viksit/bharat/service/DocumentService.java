package com.negd.viksit.bharat.service;

import com.amazonaws.services.s3.AmazonS3;
import com.negd.viksit.bharat.dto.DocumentDto;
import com.negd.viksit.bharat.dto.FileDownloadResponse;
import com.negd.viksit.bharat.model.Document;
import com.negd.viksit.bharat.model.Milestone;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.util.Base64;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.negd.viksit.bharat.repository.DocumentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentService {

    private final AmazonS3 amazonS3;
    private final DocumentRepository documentRepository;

    @Value("${cloud.aws.s3.bucket:}")
    private String bucketName;

    public DocumentDto uploadFile(MultipartFile file) throws Exception {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(bucketName, fileName, inputStream, metadata);
        }

        String fileUrl = amazonS3.getUrl(bucketName, fileName).toString();

        Document document = Document.builder()
                .fileName(fileName)
                .fileType(file.getContentType())
                .fileUrl(fileUrl)
                .size(file.getSize())
                .build();

        return new DocumentDto(documentRepository.save(document));
    }
    public FileDownloadResponse downloadFile(UUID documentId) throws IOException {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with id: " + documentId));

        String fileUrl = document.getFileUrl();
        URI uri = URI.create(fileUrl);

        String fileKey = uri.getPath().substring(1);

        S3Object s3Object = amazonS3.getObject(bucketName, fileKey);
        byte[] fileBytes;

        try (S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
            fileBytes = inputStream.readAllBytes();
        }

        String originalFileName = fileKey.substring(fileKey.lastIndexOf('/') + 1);

        return new FileDownloadResponse(fileBytes, originalFileName);


    }

    public void deleteFile(UUID id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("document not found"));

        String fileUrl = document.getFileUrl();
        URI uri = URI.create(fileUrl);

        String fileKey = uri.getPath().substring(1);

        amazonS3.deleteObject(bucketName, fileKey);
    }

    private String getExtensionFromMime(String mimeType) {
        return switch (mimeType) {
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            case "application/pdf" -> ".pdf";
            case "text/plain" -> ".txt";
            default -> "";
        };
    }

}


