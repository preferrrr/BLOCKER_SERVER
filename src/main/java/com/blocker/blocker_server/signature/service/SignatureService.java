package com.blocker.blocker_server.signature.service;

import com.blocker.blocker_server.signature.dto.response.GetSignatureResponseDto;
import com.blocker.blocker_server.board.service.S3Service;
import com.blocker.blocker_server.signature.domain.Signature;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.commons.exception.ExistsSignatureException;
import com.blocker.blocker_server.commons.exception.NotFoundException;
import com.blocker.blocker_server.commons.jwt.JwtProvider;
import com.blocker.blocker_server.signature.repository.SignatureRepository;
import com.blocker.blocker_server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class SignatureService {
    private final UserRepository userRepository;
    private final SignatureRepository signatureRepository;
    private final JwtProvider jwtProvider;
    private final S3Service s3Service;


    @Transactional
    public HttpHeaders setSignature(User user, MultipartFile file) throws IOException {

        User me = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new NotFoundException("email : " + user.getEmail()));
        // pk인 email로 조회했는데 없다면? 401 vs 404 고민했지만, 401은 jwt의 토큰 만료 response와 겹치기 때문에 404로 결정.
        if(signatureRepository.existsByUser(user))
            throw new ExistsSignatureException("email : " + user.getEmail());

        String signatureAddress = s3Service.saveSignature(file);

        Signature signature = Signature.create(me, signatureAddress);

        signatureRepository.save(signature);

        HttpHeaders headers = updateAuthority(me);

        return headers;

    }

    private HttpHeaders updateAuthority(User user) {
        //TODO : delete, insert * 2번 쿼리 생김.
        List<String> roles = user.getRoles();
        roles.add("USER"); //GUEST에서 USER 권한 추가.
        user.updateRoles(roles);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtProvider.createAccessToken(user.getEmail(), user.getName(), roles)); // access token

        return headers;
    }

    // 이전에 했던 체결된 계약들을 위해 사인들도 저장해둬야 하지 않을까?
    // => User와 Signature를 1:N으로 변경
    // => 조회할 때는 가장 최신의 Signature를 보여줌.
    // 그렇다면 전자서명 수정은 결국 새로운 전자서명을 create 하는건데, HttpMethod POST가 맞을까?
    // 일단은 수정이니까 PATCH로 함.
    @Transactional
    public void modifySignature(User user, MultipartFile file) throws IOException {
        User me = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new NotFoundException("email : " + user.getEmail()));

        String signatureAddress = s3Service.saveSignature(file);

        Signature signature = Signature.create(me, signatureAddress);

        signatureRepository.save(signature);

    }

    public GetSignatureResponseDto getSignature(User user) {
        //여러 개 중 가장 최근에 등록한 전자서명 조회
        Signature mySignature = signatureRepository.findByUserOrderByCreatedAtDesc(user).orElseThrow(()-> new NotFoundException("[get signature] email : " + user.getEmail()));

        return GetSignatureResponseDto.of(mySignature.getId().getSignatureAddress());
    }


}
