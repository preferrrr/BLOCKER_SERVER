package com.blocker.blocker_server.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.blocker.blocker_server.entity.Signature;
import com.blocker.blocker_server.entity.SignatureId;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.exception.InvalidEmailException;
import com.blocker.blocker_server.jwt.JwtProvider;
import com.blocker.blocker_server.repository.SignatureRepository;
import com.blocker.blocker_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class SignatureService {
    private final AmazonS3Client amazonS3Client;
    private final UserRepository userRepository;
    private final SignatureRepository signatureRepository;
    private final JwtProvider jwtProvider;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;

    public HttpHeaders setSignature(User user, MultipartFile file) throws IOException {

        User me = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new InvalidEmailException("email : " + user.getEmail()));

        String signaturePath = saveFile(file);

        Signature signature = new Signature();
        SignatureId id = new SignatureId();
        id.setEmail(me.getEmail());
        id.setPath(signaturePath);
        signature.setId(id);
        signature.setUser(me);

        signatureRepository.save(signature);


        List<String> roles = me.getRoles();
        roles.add("USER"); //GUEST에서 USER 권한 추가.
        me.setRoles(roles);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtProvider.createAccessToken(me.getEmail(), roles)); // access token

        return headers;

    }


    public String saveFile(MultipartFile file) throws IOException {
        String signatureName = "signature/" + UUID.randomUUID().toString() + ".png";

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getInputStream().available());

        amazonS3Client.putObject(bucket, signatureName, file.getInputStream(), objectMetadata); //s3에 전자 서명 업로드.

        String signaturePath = amazonS3Client.getUrl(bucket, signatureName).toString();

        return signaturePath;
    }
}
