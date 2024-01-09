package com.example.minioimplement.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.user}")
    private String user;

    @Value("${minio.password}")
    private String password;

    public static final String VIDEO_BUCKET_NAME = "video";

    public static final String PDF_BUCKET_NAME="pdf";

    public AWSCredentials credentials() {
        AWSCredentials credentials = new BasicAWSCredentials(user,password);
        return credentials;
    }
    @Bean
    public AmazonS3 amazonS3Client(){
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint,Regions.AP_SOUTHEAST_2.toString()))
                .withCredentials(new AWSStaticCredentialsProvider(credentials()))
                .build();
        return s3client;
    }


}
