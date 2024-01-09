package com.example.minioimplement.service;

import com.example.minioimplement.configuration.AmazonS3Config;
import com.example.minioimplement.model.FileMetadata;
import com.example.minioimplement.model.Range;
import com.example.minioimplement.repository.FileMetadataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.naming.factory.LookupFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DefaultVideoService implements VideoService{

    private final AmazonS3Service amazonS3Service;

    private final FileMetadataRepository fileMetadataRepository;

    private final Logger log = LoggerFactory.getLogger(DefaultVideoService.class);
    @Override
    @Transactional
    public UUID save(MultipartFile video) {
        try {
            UUID fileUuid = UUID.randomUUID();
            FileMetadata metadata = FileMetadata.builder()
                    .id(fileUuid.toString())
                    .size(video.getSize())
                    .bucketName(AmazonS3Config.VIDEO_BUCKET_NAME)
                    .fileName(video.getName())
                    .httpContentType(video.getContentType())
                    .build();
            fileMetadataRepository.save(metadata);
            amazonS3Service.saveVideoStream(fileUuid,video,AmazonS3Config.VIDEO_BUCKET_NAME);
            return fileUuid;
        } catch (Exception ex) {
            log.error("Exception occurred when trying to save the file", ex);
            return null;
        }
    }

    @Override
    public ChunkWithMetadata fetchChunk(UUID uuid, Range range) {
        FileMetadata fileMetadata = fileMetadataRepository.findById(uuid.toString()).orElseThrow();
        return new ChunkWithMetadata(fileMetadata, readChunk(uuid, range, fileMetadata.getSize(),fileMetadata.getBucketName()));
    }

    private byte[] readChunk(UUID uuid, Range range, long fileSize,String bucketName) {
        long startPosition = range.getRangeStart();
        long endPosition = range.getRangeEnd(fileSize);
        int chunkSize = (int) (endPosition - startPosition + 1);
        try(InputStream inputStream = amazonS3Service.getInputStream(uuid, startPosition, chunkSize,bucketName)) {
            return inputStream.readAllBytes();
        } catch (Exception exception) {
            log.error("Exception occurred when trying to read file with ID = {}", uuid);
            return null;
        }
    }

    public record ChunkWithMetadata(
            FileMetadata metadata,
            byte[] chunk
    ) {}
}
