package com.blocker.blocker_server.signature.service;

import com.blocker.blocker_server.Image.service.S3Service;
import com.blocker.blocker_server.commons.utils.CurrentUserGetter;
import com.blocker.blocker_server.signature.domain.Signature;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.service.UserServiceSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SignatureServiceTest {

    @InjectMocks
    private SignatureService signatureService;
    @Mock
    private SignatureServiceSupport signatureServiceSupport;
    @Mock
    private UserServiceSupport userServiceSupport;
    @Mock
    private S3Service s3Service;
    @Mock
    private CurrentUserGetter currentUserGetter;

    private User user;
    private Signature signature;

    @BeforeEach
    void setUp() {
        user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("GUEST", "USER"));
        signature = Signature.create(user, "testAddress");
    }

    @DisplayName("User의 Signature를 등록한다.")
    @Test
    void setSignature() throws IOException {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(userServiceSupport.getUserByEmail(anyString())).willReturn(user);
        willDoNothing().given(signatureServiceSupport).checkAlreadyHaveSignature(any(User.class));
        given(s3Service.saveSignature(any(MultipartFile.class))).willReturn("testAddress");
        willDoNothing().given(signatureServiceSupport).saveSignature(any(Signature.class));
        given(signatureServiceSupport.updateToUserAuthority(any(User.class))).willReturn(mock(HttpHeaders.class));

        /** when */

        signatureService.setSignature(mock(MultipartFile.class));

        /** then */

        verify(userServiceSupport, timeout(1)).getUserByEmail(anyString());
        verify(signatureServiceSupport, times(1)).checkAlreadyHaveSignature(any(User.class));
        verify(s3Service, times(1)).saveSignature(any(MultipartFile.class));
        verify(signatureServiceSupport, times(1)).saveSignature(any(Signature.class));
        verify(signatureServiceSupport, times(1)).updateToUserAuthority(any(User.class));

    }

    @DisplayName("User의 Signature를 수정한다.")
    @Test
    void modifySignature() throws IOException {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(userServiceSupport.getUserByEmail(anyString())).willReturn(user);
        given(s3Service.saveSignature(any(MultipartFile.class))).willReturn("testAddress");
        willDoNothing().given(signatureServiceSupport).saveSignature(any(Signature.class));

        /** when */

        signatureService.modifySignature(mock(MultipartFile.class));

        /** then */

        verify(userServiceSupport, timeout(1)).getUserByEmail(anyString());
        verify(s3Service, times(1)).saveSignature(any(MultipartFile.class));
        verify(signatureServiceSupport, times(1)).saveSignature(any(Signature.class));

    }


    @DisplayName("나의 Signature를 조회한다.")
    @Test
    void getSignature() throws IOException {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(signatureServiceSupport.getMySignature(any(User.class))).willReturn(signature);

        /** when */

        signatureService.getSignature();

        /** then */

        verify(signatureServiceSupport, times(1)).getMySignature(any(User.class));

    }

}