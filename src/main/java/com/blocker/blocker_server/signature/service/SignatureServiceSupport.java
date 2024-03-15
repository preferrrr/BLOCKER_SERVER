package com.blocker.blocker_server.signature.service;

import com.blocker.blocker_server.commons.jwt.JwtProvider;
import com.blocker.blocker_server.signature.domain.Signature;
import com.blocker.blocker_server.signature.exception.AlreadyHaveSignatureException;
import com.blocker.blocker_server.signature.exception.SignatureNotFoundException;
import com.blocker.blocker_server.signature.repository.SignatureRepository;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignatureServiceSupport {

    private final SignatureRepository signatureRepository;
    private final JwtProvider jwtProvider;

    public void checkAlreadyHaveSignature(User user) {
        if(signatureRepository.existsByUser(user))
            throw new AlreadyHaveSignatureException();
    }

    @Transactional
    public void saveSignature(Signature signature) {
        signatureRepository.save(signature);
    }

    @Transactional
    public HttpHeaders updateToUserAuthority(User user) {
        List<String> roles = List.of("USER");
        //roles.add("USER"); //GUEST에서 USER 권한 추가.
        user.updateRoles(roles);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtProvider.createAccessToken(user.getEmail(), user.getName(), roles)); // access token

        return headers;
    }


    public Signature getMySignature(User user) {
        return  signatureRepository.findByUserOrderByCreatedAtDesc(user)
                .orElseThrow(SignatureNotFoundException::new);
    }
}
