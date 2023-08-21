package com.blocker.blocker_server.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class S3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;

    public String saveFile(MultipartFile file) throws IOException {
        String signatureName = "signature/" + UUID.randomUUID().toString()+".png";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getInputStream().available());

        amazonS3Client.putObject(bucket, signatureName, file.getInputStream(), objectMetadata); //s3에 전자 서명 업로드.

        String signaturePath = amazonS3Client.getUrl(bucket, signatureName).toString();
        return signaturePath;
    }
}
