package com.blocker.blocker_server.user.service;

import com.blocker.blocker_server.commons.jwt.JwtProvider;
import com.blocker.blocker_server.user.exception.EmptyRefreshTokenException;
import com.blocker.blocker_server.user.exception.InvalidRefreshTokenException;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.dto.response.SearchUserResponse;
import com.blocker.blocker_server.user.exception.UserNotFoundException;
import com.blocker.blocker_server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserServiceSupport {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public List<User> searchUsers(String keyword) {
        return userRepository.searchUsers(keyword);
    }

    public List<SearchUserResponse> createSearchUserResponse(List<User> userList) {
        return userList.stream()
                .map(user -> SearchUserResponse.of(user.getEmail(), user.getName(), user.getPicture()))
                .collect(Collectors.toList());
    }

    public User getUserByRefreshTokenValue(String token) {
        String value = jwtProvider.getRefreshTokenValue(token);
        return userRepository.findByRefreshtokenValue(value).orElseThrow(InvalidRefreshTokenException::new);
    }


    public HttpHeaders createHeadersWithJwt(String email, String username, String refreshtokenValue, List<String> roles) {
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

    public String getRefreshTokenFromCookie(String cookie) {
        return cookie.substring(13, cookie.indexOf(";"));
    }

    public String getNewRefreshTokenValue() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public void checkIsEmptyRefreshToken(String cookie) {
        if (cookie.isEmpty() || cookie.isBlank())
            throw new EmptyRefreshTokenException();
    }
}
