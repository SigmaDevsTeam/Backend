package com.sigmadevs.testtask.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.internal.util.Ec2MetadataConfigProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

@Configuration
public class BucketConfig {
    @Value("${aws.access_key}")
    private String accessKey;
    @Value("${aws.secret_access_key}")
    private String secretAccessKey;
    @Value("${aws.region}")
    private String region;


//    @Bean
//    public S3Client getClient() {
//        CreateBucketRequest.builder().bucket("asd").build();
//        S3Client build = S3Client.builder().credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretAccessKey))).region(Region.EU_NORTH_1).build();
//
//        return ;
//    }

}