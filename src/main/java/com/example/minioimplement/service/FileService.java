package com.example.minioimplement.service;

import com.example.minioimplement.exception.StorageException;
import com.example.minioimplement.model.FileMetadata;
import com.example.minioimplement.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioStorageService storageService;

    private final FileMetadataRepository fileMetadataRepository;

    private Logger log = LoggerFactory.getLogger(DefaultVideoService.class);

    public UUID save(MultipartFile file) throws IOException {
        try {
            UUID uuid = UUID.randomUUID();
            FileMetadata metadata = FileMetadata.builder()
                    .id(uuid.toString())
                    .size(file.getSize())
                    .httpContentType(file.getContentType())
                    .build();
            fileMetadataRepository.save(metadata);
            storageService.savePdfFile(file.getInputStream(),uuid);
            return uuid;
        } catch (Exception ex){
            log.error("Exception occurred when trying to save the file", ex);
            throw new StorageException(ex);
        }
    }

    public InputStream getPdfFile(UUID uuid){
        try {
            Optional<FileMetadata> fileMetadata = fileMetadataRepository.findById(uuid.toString());
            if(!fileMetadata.isPresent()){
                log.info("No found filemeta data with uuid"+uuid.toString());
            }
            InputStream inputStream = storageService.getPdfFile(uuid);
            return inputStream;
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
