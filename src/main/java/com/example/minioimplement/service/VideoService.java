package com.example.minioimplement.service;

import com.example.minioimplement.model.Range;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface VideoService {
    UUID save(MultipartFile video);

    DefaultVideoService.ChunkWithMetadata fetchChunk(UUID uuid, Range range);

}
