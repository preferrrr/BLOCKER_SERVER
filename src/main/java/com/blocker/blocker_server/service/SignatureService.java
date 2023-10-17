package com.blocker.blocker_server.service;

import com.blocker.blocker_server.entity.Signature;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.exception.ExistsSignatureException;
import com.blocker.blocker_server.exception.NotFoundException;
import com.blocker.blocker_server.jwt.JwtProvider;
import com.blocker.blocker_server.repository.SignatureRepository;
import com.blocker.blocker_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class SignatureService {
    private final UserRepository userRepository;
    private final SignatureRepository signatureRepository;
    private final JwtProvider jwtProvider;
    private final S3Service s3Service;


    public HttpHeaders setSignature(User user, MultipartFile file) throws IOException {

        User me = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new NotFoundException("email : " + user.getEmail()));
        // pk인 email로 조회했는데 없다면? 401 vs 404 고민했지만, 401은 jwt의 토큰 만료 response와 겹치기 때문에 404로 결정.
        if(signatureRepository.existsByUser(user))
            throw new ExistsSignatureException("email : " + user.getEmail());

        String signatureAddress = s3Service.saveSignature(file);

        Signature signature = Signature.builder()
                .email(me.getEmail())
                .signatureAddress(signatureAddress)
                .user(me)
                .build();


        signatureRepository.save(signature);

        //TODO : delete, insert * 2번 쿼리 생김.
        List<String> roles = me.getRoles();
        roles.add("USER"); //GUEST에서 USER 권한 추가.
        me.updateRoles(roles);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtProvider.createAccessToken(me.getEmail(), roles)); // access token

        return headers;

    }

    // 이전에 했던 체결된 계약들을 위해 사인들도 저장해둬야 하지 않을까?
    // => User와 Signature를 1:N으로 변경
    // => 조회할 때는 가장 최신의 Signature를 보여줌.
    // 그렇다면 전자서명 수정은 결국 새로운 전자서명을 create 하는건데, HttpMethod POST가 맞을까?
    // 일단은 수정이니까 PATCH로 함.
    public void modifySignature(User user, MultipartFile file) throws IOException {
        User me = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new NotFoundException("email : " + user.getEmail()));

        String signatureAddress = s3Service.saveSignature(file);

        Signature signature = Signature.builder()
                .email(me.getEmail())
                .signatureAddress(signatureAddress)
                .user(me)
                .build();

        signatureRepository.save(signature);

    }



}
