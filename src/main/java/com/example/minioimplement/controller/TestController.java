package com.example.minioimplement.controller;

import com.amazonaws.services.s3.model.Bucket;
import com.example.minioimplement.service.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final AmazonS3Service amazonS3Service;

    @GetMapping("/listbucket")
    public ResponseEntity<List<String>> getListBuckets(){
        List<Bucket> buckets = amazonS3Service.listBuckets();
        List<String> bucketStringList = new ArrayList<>();
        for (Bucket bucket : buckets) {
            bucketStringList.add(bucket.getName());
        }
        return ResponseEntity.ok(bucketStringList);
    }
}
