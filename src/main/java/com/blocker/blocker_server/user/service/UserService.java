package com.blocker.blocker_server.user.service;

import com.blocker.blocker_server.user.dto.request.LoginRequestDto;
import com.blocker.blocker_server.user.dto.response.SearchUserResponse;
import com.blocker.blocker_server.signature.repository.SignatureRepository;
import com.blocker.blocker_server.user.repository.UserRepository;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserServiceSupport userServiceSupport;
    private final UserRepository userRepository;
    private final SignatureRepository signatureRepository;

    @Transactional
    public ResponseEntity<HttpHeaders> login(LoginRequestDto requestDto) {

        //이메일로 조회해서
        //DB에 있으면 로그인처리(200, token 2개) user.getRoles로 토큰에 roles 세팅.
        //만약 전자서명 안 했으면 roles가 GUEST라서 다른 요청 불가.

        //DB에 없으면 db에 저장하고 201 CREATED, role = GUEST, 프론트에서 전자서명저장 api 호출해서 저장하면 role에 user 추가

        String email = requestDto.getEmail();

        Optional<User> findUser = userRepository.findByEmail(email);

        List<String> roles;

        String refreshtokenValue = UUID.randomUUID().toString().replaceAll("-", "");

        if (findUser.isEmpty()) { // 새로운 유저

            roles = new ArrayList<>();
            roles.add("GUEST");

            User newUser = User.create(email, requestDto.getName(), requestDto.getPicture(),refreshtokenValue,roles);

            userRepository.save(newUser);

            HttpHeaders headers = userServiceSupport.createHeadersWithJwt(email, newUser.getName(), refreshtokenValue, roles);

            return new ResponseEntity<>(headers, HttpStatus.CREATED); // created이면 프론트에서 전자서명등록하는 페이지로 가도록.


        } else { // 이미 가입한 유저
            User me = findUser.get();
            roles = me.getRoles();
            String username = me.getName();

            HttpHeaders headers = userServiceSupport.createHeadersWithJwt(email, username, refreshtokenValue, roles);

            if(!me.getName().equals(requestDto.getName()))
                me.updateName(requestDto.getName());

            if(!me.getPicture().equals(requestDto.getPicture()))
                me.updatePicture(requestDto.getPicture());

            me.setRefreshtokenValue(refreshtokenValue);

            if(!signatureRepository.existsByUser(me))
                return new ResponseEntity<>(headers,HttpStatus.CREATED);

            return new ResponseEntity<>(headers, HttpStatus.OK);

        }
    }


    public HttpHeaders reissueToken(String cookie) {

        String token = userServiceSupport.getRefreshTokenFromCookie(cookie);

        //value(unique key)로 유저 찾음
        User user = userServiceSupport.getUserByRefreshTokenValue(token);

        //새로운 리프레시 토큰 value
        String newRefreshTokenValue = userServiceSupport.getNewRefreshTokenValue();

        //리프레시 토큰도 재발급
        user.setRefreshtokenValue(newRefreshTokenValue);

        //새로운 Jwt가 포함된 헤더 반환
        return userServiceSupport.createHeadersWithJwt(user.getEmail(),user.getName(),newRefreshTokenValue,user.getRoles());

    }

    public List<SearchUserResponse> searchUsers(String keyword) {

        //keyword가 포함된 이메일 또는 닉네임을 가진 유저 리스트
        List<User> userList = userServiceSupport.searchUsers(keyword);

        //dto로 변환해서 반환
        return userServiceSupport.createSearchUserResponse(userList);

    }
}