package com.blocker.blocker_server.service;

import com.blocker.blocker_server.dto.LoginRequestDto;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.exception.NotFoundException;
import com.blocker.blocker_server.jwt.JwtProvider;
import com.blocker.blocker_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

        String refreshtokenValue = UUID.randomUUID().toString().replaceAll("-","");

        if(findUser.isEmpty()) { // 새로운 유저
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
            User user = findUser.get();
            roles = user.getRoles();

            HttpHeaders headers = createHeaders(email, refreshtokenValue, roles);

            user.setRefreshtokenValue(refreshtokenValue);

            return new ResponseEntity<>(headers, HttpStatus.OK);

        }
    }

    public HttpHeaders createHeaders(String email, String refreshtokenValue ,List<String> roles) {
        HttpHeaders headers = new HttpHeaders();

        headers.add("authorization", "Bearer " + jwtProvider.createAccessToken(email, roles)); // access token

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
}
