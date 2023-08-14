package com.blocker.blocker_server.service;

import com.blocker.blocker_server.dto.LoginRequestDto;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.exception.FailSaveSignatureException;
import com.blocker.blocker_server.exception.InvalidEmailException;
import com.blocker.blocker_server.exception.InvalidRefreshTokenException;
import com.blocker.blocker_server.exception.NotFoundException;
import com.blocker.blocker_server.jwt.JwtProvider;
import com.blocker.blocker_server.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public ResponseEntity<?> login(LoginRequestDto requestDto) {

        //이메일로 조회해서
        //DB에 있으면 로그인처리(200, token 2개) user.getRoles로 토큰에 roles 세팅.
        //만약 전자서명 안 했으면 roles가 GUEST라서 다른 요청 불가.

        //DB에 없으면 db에 저장하고 201 CREATED, role = GUEST, 프론트에서 전자서명저장 api 호출해서 저장하면 role에 user 추가

        String email = requestDto.getEmail();

        Optional<User> findUser = userRepository.findByEmail(email);

        List<String> roles;

        String refreshtokenValue = UUID.randomUUID().toString().replaceAll("-", "");

        if (findUser.isEmpty()) { // 새로운 유저
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPicture(requestDto.getPicture());
            newUser.setName(requestDto.getName());
            roles = new ArrayList<>();
            roles.add("GUEST");
            newUser.setSignature(null);
            newUser.setRoles(roles);
            newUser.setRefreshtokenValue(refreshtokenValue);

            userRepository.save(newUser);

            HttpHeaders headers = createHeaders(email, refreshtokenValue, roles);

            return new ResponseEntity<>(headers, HttpStatus.CREATED); // created이면 프론트에서 전자서명등록하는 페이지로 가도록.


        } else { // 이미 가입한 유저
            //User user = userRepository.findById(email).orElseThrow(()->new NotFoundException("[login] email : " + email));
            User me = findUser.get();
            roles = me.getRoles();

            HttpHeaders headers = createHeaders(email, refreshtokenValue, roles);

            me.setRefreshtokenValue(refreshtokenValue);

            return new ResponseEntity<>(headers, HttpStatus.OK);

        }
    }

    public HttpHeaders createHeaders(String email, String refreshtokenValue, List<String> roles) {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + jwtProvider.createAccessToken(email, roles)); // access token

        ResponseCookie cookie = ResponseCookie.from("refreshToken", jwtProvider.createRefreshToken(refreshtokenValue))
                .maxAge(14 * 24 * 60 * 60)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        headers.add(HttpHeaders.COOKIE, cookie.toString()); // refresh token

        return headers;
    }


    public void setSignature(User user, MultipartFile file) {

        User me = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new InvalidEmailException("email : " + user.getEmail()));

        //TODO : 나중에는 S3에 저장
        String filePath = "C:\\test\\" + file.getOriginalFilename();

        try {
            Path destination = new File(filePath).toPath();
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FailSaveSignatureException("email : " + user.getEmail());
        }

        me.setSignature(filePath);
        List<String> roles = me.getRoles();
        roles.add("USER");
        me.setRoles(roles);

    }

    public HttpHeaders reissueToken(String cookie) {

        String token = cookie.substring(13, cookie.indexOf(";"));

        String value = jwtProvider.getRefreshTokenValue(token); // 토큰 유효성 검사까지 함.

        //value로 유저 찾고, 그 유저 이메일로 엑세스 토큰 재발급.

        User user = userRepository.findByRefreshtokenValue(value).orElseThrow(() -> new InvalidRefreshTokenException("[not exists value] token : " + token));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtProvider.createAccessToken(user.getEmail(), user.getRoles())); // access token

        return headers;

    }
}
