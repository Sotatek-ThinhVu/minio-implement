package com.example.minioimplement.configuration;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.user}")
    private String user;

    @Value("${minio.password}")
    private String password;

    public static final String COMMON_BUCKET_NAME = "common";

    public static final String PDF_BUCKET_NAME="pdf";
    @Bean
    public MinioClient minioClient() throws Exception {
        MinioClient client = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(user, password)
                .build();
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(COMMON_BUCKET_NAME).build())) {
            client.makeBucket(
                    MakeBucketArgs
                            .builder()
                            .bucket(COMMON_BUCKET_NAME)
                            .build()
            );
        }
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(PDF_BUCKET_NAME).build())) {
            client.makeBucket(
                    MakeBucketArgs
                            .builder()
                            .bucket(PDF_BUCKET_NAME)
                            .build()
            );
        }
        return client;
    }
}
