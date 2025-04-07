package com.dormventory.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfig {
    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    private AWSStaticCredentialsProvider getCredentialsProvider() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return new AWSStaticCredentialsProvider(awsCredentials);
    }

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(getCredentialsProvider())
            .build();
    }

    @Bean
    public AmazonTextract amazonTextract() {
        return AmazonTextractClientBuilder.standard()
            .withRegion(region)
            .withCredentials(getCredentialsProvider())
            .build();
    }
} 