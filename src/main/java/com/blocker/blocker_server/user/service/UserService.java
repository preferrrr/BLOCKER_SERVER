package com.blocker.blocker_server.user.service;

import com.blocker.blocker_server.user.dto.request.LoginRequestDto;
import com.blocker.blocker_server.user.dto.response.SearchUserResponse;
import com.blocker.blocker_server.commons.exception.InvalidRefreshTokenException;
import com.blocker.blocker_server.commons.jwt.JwtProvider;
import com.blocker.blocker_server.signature.repository.SignatureRepository;
import com.blocker.blocker_server.user.repository.UserRepository;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final SignatureRepository signatureRepository;

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

            User newUser = User.builder()
                    .email(email)
                    .picture(requestDto.getPicture())
                    .name(requestDto.getName())
                    .roles(roles)
                    .refreshtokenValue(refreshtokenValue)
                    .build();

            userRepository.save(newUser);

            HttpHeaders headers = createHeaders(email, newUser.getUsername(), refreshtokenValue, roles);

            return new ResponseEntity<>(headers, HttpStatus.CREATED); // created이면 프론트에서 전자서명등록하는 페이지로 가도록.


        } else { // 이미 가입한 유저
            User me = findUser.get();
            roles = me.getRoles();
            String username = me.getUsername();

            HttpHeaders headers = createHeaders(email, username, refreshtokenValue, roles);

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

    public HttpHeaders createHeaders(String email, String username, String refreshtokenValue, List<String> roles) {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + jwtProvider.createAccessToken(email, username, roles)); // access token

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




    public HttpHeaders reissueToken(String cookie) {

        String token = cookie.substring(13, cookie.indexOf(";"));

        String value = jwtProvider.getRefreshTokenValue(token); // 토큰 유효성 검사까지 함.

        //value로 유저 찾고, 그 유저 이메일로 엑세스 토큰 재발급.

        User user = userRepository.findByRefreshtokenValue(value).orElseThrow(() -> new InvalidRefreshTokenException("[not exists value] token : " + token));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwtProvider.createAccessToken(user.getEmail(), user.getName(), user.getRoles())); // access token

        return headers;

    }

    public List<SearchUserResponse> searchUsers(String keyword) {

        List<User> userList = userRepository.searchUsers(keyword);

        List<SearchUserResponse> result = new ArrayList<>();

        for (User user : userList) {
            SearchUserResponse response = SearchUserResponse.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .picture(user.getPicture())
                    .build();
            result.add(response);
        }

        return result;

    }
}