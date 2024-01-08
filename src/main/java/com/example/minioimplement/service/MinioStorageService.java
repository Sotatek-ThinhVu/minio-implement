package com.example.minioimplement.service;

import com.example.minioimplement.configuration.MinioConfig;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.messages.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioStorageService {

    private final MinioClient minioClient;

    @Value("${minio.put-object-part-size}")
    private Long putObjectPartSize;

    public void save(MultipartFile file, UUID uuid) throws Exception {
        minioClient.putObject(
                PutObjectArgs
                        .builder()
                        .bucket(MinioConfig.COMMON_BUCKET_NAME)
                        .object(uuid.toString())
                        .stream(file.getInputStream(), file.getSize(), putObjectPartSize)
                        .build()
        );
    }
    public InputStream getInputStream(UUID uuid, long offset, long length) throws Exception {
        return minioClient.getObject(
                GetObjectArgs
                        .builder()
                        .bucket(MinioConfig.COMMON_BUCKET_NAME)
                        .offset(offset)
                        .length(length)
                        .object(uuid.toString())
                        .build());
    }

    public void savePdfFile(InputStream file, UUID uuid) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(MinioConfig.PDF_BUCKET_NAME)
                        .object(uuid.toString())
                        .stream(file,-1,10485760)
                        .build()
        );
    }

    public InputStream getPdfFile(UUID uuid) throws Exception {
        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder().bucket(MinioConfig.PDF_BUCKET_NAME).object(uuid.toString()).build());
        return stream;
    }

    public List<String> listBuckets() throws Exception {
        List<Bucket> list = minioClient.listBuckets();
        List<String> names = new ArrayList<>();

        list.forEach(bucket -> {
            names.add(bucket.name());
        });
        return names;
    }
}
