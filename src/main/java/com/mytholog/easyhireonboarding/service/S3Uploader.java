package com.mytholog.easyhireonboarding.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Uploader {
    private final S3Client s3Client;

    @Value("${app.s3.bucket}")
    private String bucketName;

    public S3Uploader(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadZip(byte[] zipBytes, String objectKey) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .contentType("application/zip")
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(zipBytes));

        return String.format("s3://%s/%s", bucketName, objectKey);
    }
}
