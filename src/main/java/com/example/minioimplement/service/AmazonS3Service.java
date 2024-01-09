package com.example.minioimplement.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AmazonS3Service {
    private final AmazonS3 amazonS3Client;

    private final Logger log = LoggerFactory.getLogger(AmazonS3Service.class);

    public void createS3Bucket(String bucketName) {
        if(!amazonS3Client.doesBucketExist(bucketName)) {
            amazonS3Client.createBucket(bucketName);
            log.info("Bucket name have been created");
        }
        return;
    }

    public List<Bucket> listBuckets(){
        return amazonS3Client.listBuckets();
    }

    public void deleteBucket(String bucketName){
        try {
            amazonS3Client.deleteBucket(bucketName);
        } catch (AmazonServiceException e) {
            log.error(e.getErrorMessage());
            return;
        }
    }

    public void saveVideoStream(UUID uuid, MultipartFile file,String bucketName ) throws Exception {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType("video/mp4");
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,uuid.toString(),file.getInputStream(),objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead);
        try {
            createS3Bucket(bucketName);
            amazonS3Client.putObject(putObjectRequest);
        } catch (Exception ex){
            log.error("Save video stream error: "+ex.getMessage());
        }
    }

    public InputStream getInputStream(UUID uuid,long offset,long length,String bucketName){
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName,uuid.toString());
        getObjectRequest.withRange(offset,offset+length);
        try {
            return amazonS3Client.getObject(getObjectRequest).getObjectContent();
        } catch (Exception ex){
            log.error("Error when get video stream "+ex.getMessage());
        }
        return null;
    }

    public void savePdfFile(InputStream file, UUID uuid,String bucketName) throws Exception {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.available());
        objectMetadata.setContentType("application/pdf");
        System.out.println("here here");
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,uuid.toString(),file,objectMetadata);
        try {
            createS3Bucket(bucketName);
            amazonS3Client.putObject(putObjectRequest);
        } catch (Exception ex){
            log.error("Error when save pdf file "+ ex.getMessage());
        }
    }

    public InputStream getPdfFile(UUID uuid,String bucketName){
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName,uuid.toString());

        try {
            return amazonS3Client.getObject(getObjectRequest).getObjectContent();
        } catch (Exception ex){
            log.error("Error when get pdf file "+ex.getMessage());
        }
        return null;
    }
}
