package com.example.minioimplement.service;

import com.example.minioimplement.configuration.AmazonS3Config;
import com.example.minioimplement.dto.FileDto;
import com.example.minioimplement.model.FileMetadata;
import com.example.minioimplement.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class FileService {


    private final AmazonS3Service amazonS3Service;

    private final FileMetadataRepository fileMetadataRepository;

    private Logger log = LoggerFactory.getLogger(DefaultVideoService.class);

    public UUID save(MultipartFile file) {
        try {
            UUID uuid = UUID.randomUUID();
            FileMetadata metadata = FileMetadata.builder()
                    .id(uuid.toString())
                    .fileName(file.getName())
                    .bucketName(AmazonS3Config.PDF_BUCKET_NAME)
                    .size(file.getSize())
                    .httpContentType(file.getContentType())
                    .build();
            fileMetadataRepository.save(metadata);
            amazonS3Service.savePdfFile(file.getInputStream(),uuid,AmazonS3Config.PDF_BUCKET_NAME);
            return uuid;
        } catch (Exception ex){
            log.error("Exception occurred when trying to save the file", ex);
        }
        return null;
    }

    public FileDto getPdfFile(UUID uuid){
        try {
            FileMetadata fileMetadata = fileMetadataRepository.findById(uuid.toString()).orElseThrow();
            InputStream inputStream = amazonS3Service.getPdfFile(uuid,AmazonS3Config.PDF_BUCKET_NAME);
            FileDto file = new FileDto(fileMetadata.getFileName(),inputStream);
            return file;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
