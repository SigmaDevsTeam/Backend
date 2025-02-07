package com.sigmadevs.testtask.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    private final S3Client s3Client;
    String bucketName = "test-task-h";

    public String uploadImage(String folder, MultipartFile file) {
        String filename = file.getOriginalFilename();
        String url = folder + "/" + UUID.randomUUID().toString() + filename;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(url).build();
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            log.debug(e.getMessage());
            throw new RuntimeException(e);
        }
        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(url)).toString();
    }
}
