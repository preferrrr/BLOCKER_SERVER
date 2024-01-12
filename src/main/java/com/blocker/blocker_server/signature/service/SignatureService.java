package com.blocker.blocker_server.signature.service;

import com.blocker.blocker_server.signature.dto.response.GetSignatureResponseDto;
import com.blocker.blocker_server.Image.service.S3Service;
import com.blocker.blocker_server.signature.domain.Signature;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.service.UserServiceSupport;
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

    private final SignatureServiceSupport signatureServiceSupport;
    private final UserServiceSupport userServiceSupport;
    private final S3Service s3Service;


    @Transactional
    public HttpHeaders setSignature(User user, MultipartFile file) throws IOException {

        User me = userServiceSupport.findUserByEmail(user.getEmail());

        //전자서명 이미 가지고 있는지 검사
        signatureServiceSupport.checkAlreadyHaveSignature(me);

        //S3에 전자서명 저장 후 주소
        String signatureAddress = s3Service.saveSignature(file);

        Signature signature = Signature.create(me, signatureAddress);

        //전자서명 저장
        signatureServiceSupport.saveSignature(signature);

        //JWT에 USER 권한 추가해서 반환
        return signatureServiceSupport.updateToUserAuthority(me);
    }

    // 이전에 했던 체결된 계약들을 위해 사인들도 저장해둬야 하지 않을까?
    // => User와 Signature를 1:N으로 변경
    // => 조회할 때는 가장 최신의 Signature를 보여줌.
    // 그렇다면 전자서명 수정은 결국 새로운 전자서명을 create 하는건데, HttpMethod POST가 맞을까?
    // 일단은 수정이니까 PATCH로 함.
    @Transactional
    public void modifySignature(User user, MultipartFile file) throws IOException {

        User me = userServiceSupport.findUserByEmail(user.getEmail());

        //새로운 전자서명 S3에 저장
        String signatureAddress = s3Service.saveSignature(file);

        Signature signature = Signature.create(me, signatureAddress);

        //새로운 전자서명 DB에 저장
        signatureServiceSupport.saveSignature(signature);
    }

    public GetSignatureResponseDto getSignature(User user) {
        //여러 개 중 가장 최근에 등록한 전자서명 조회
        Signature mySignature = signatureServiceSupport.getMySignature(user);

        return GetSignatureResponseDto.of(mySignature.getId().getSignatureAddress());
    }


}
