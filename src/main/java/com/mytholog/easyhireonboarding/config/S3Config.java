package com.mytholog.easyhireonboarding.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${s3.access.key}")
    private String accessKey;

    @Value("${s3.secret.accesskey}")
    private String secretKey;

    @Bean
    public S3Client s3Client() {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                accessKey, // Replace with your AWS Access Key
                secretKey // Replace with your AWS Secret Key
        );
//        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
//        AwsCredentialsProvider awsCredentialsProvider = ProfileCredentialsProvider.create(String.valueOf(awsBasicCredentials));
        return S3Client.builder()
                .region(Region.AP_SOUTH_1) // Use your region
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds)) // or use EnvironmentVariableCredentialsProvider
                .build();
    }
}
