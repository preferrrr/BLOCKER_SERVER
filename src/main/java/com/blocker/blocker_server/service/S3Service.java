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

@Service
@Transactional
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;


    public String saveSignature(MultipartFile file) throws IOException {
        String signatureName = "signature/" + UUID.randomUUID().toString() + ".png";

        String signaturePath = save(signatureName, file);

        return signaturePath;
    }

    public String saveImage(MultipartFile file) throws IOException {
        String imageName = UUID.randomUUID().toString() + ".png";
        String imagePath = save(imageName, file);

        return imagePath;
    }

    public String save(String name, MultipartFile file) throws IOException {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getInputStream().available());

        amazonS3Client.putObject(bucket, name, file.getInputStream(), objectMetadata); //s3에 전자 서명 업로드.

        String result = amazonS3Client.getUrl(bucket, name).toString();

        return result;
    }
}
