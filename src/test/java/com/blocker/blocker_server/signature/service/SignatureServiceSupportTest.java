package com.blocker.blocker_server.signature.service;

import com.blocker.blocker_server.IntegrationTestSupport;
import com.blocker.blocker_server.commons.exception.ExistsSignatureException;
import com.blocker.blocker_server.commons.jwt.JwtProvider;
import com.blocker.blocker_server.signature.domain.Signature;
import com.blocker.blocker_server.signature.exception.AlreadyHaveSignatureException;
import com.blocker.blocker_server.signature.exception.SignatureNotFoundException;
import com.blocker.blocker_server.signature.repository.SignatureRepository;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

class SignatureServiceSupportTest extends IntegrationTestSupport {

    @Autowired
    SignatureServiceSupport signatureServiceSupport;
    @Autowired
    private SignatureRepository signatureRepository;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        signatureRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("이미 전자서명을 가지고 있으면 AlreadyHaveSignatureException을 던진다.")
    @Test
    void checkAlreadyHaveSignature() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Signature signature = Signature.create(user, "testAddress");
        signatureRepository.save(signature);

        /** when then */

        assertThatThrownBy(() -> signatureServiceSupport.checkAlreadyHaveSignature(user))
                .isInstanceOf(AlreadyHaveSignatureException.class);

    }

    @DisplayName("signature를 저장한다.")
    @Test
    void saveSignature() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("GUEST"));
        userRepository.save(user);

        Signature signature = Signature.create(user, "testAddress");

        /** when */

        signatureServiceSupport.saveSignature(signature);

        /** then */
        List<Signature> signatures = signatureRepository.findAll();
        assertThat(signatures).hasSize(1);
        assertThat(signatures.get(0).getUser().getEmail()).isEqualTo(user.getEmail());

    }

    @DisplayName("User 권한이 추가된 Access token이 포함된 헤더를 반환하고, DB에 User 권한이 추가된다.. ")
    @Test
    void updateToUserAuthority() {

        /** given */

        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("GUEST"));
        userRepository.save(user);

        /** when */

        HttpHeaders result = signatureServiceSupport.updateToUserAuthority(user);

        /** then */

        assertThat(result.containsKey("Authorization")).isTrue();

        String token = String.valueOf(result.get("Authorization")).substring(7);
        assertThat(jwtProvider.getRoles(token).contains("USER")).isTrue();

    }

    @DisplayName("나의 전자서명을 조회한다.")
    @Test
    void getMySignature() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        Signature signature = Signature.create(user, "testAddress");
        signatureRepository.save(signature);
        /** when */

        Signature mySignature = signatureServiceSupport.getMySignature(user);

        /** then */

        assertThat(mySignature.getUser().getEmail()).isEqualTo(user.getEmail());

    }


}