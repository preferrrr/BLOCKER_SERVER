package com.blocker.blocker_server.user.service;

import com.blocker.blocker_server.IntegrationTestSupport;
import com.blocker.blocker_server.commons.jwt.JwtProvider;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.dto.response.SearchUserResponse;
import com.blocker.blocker_server.user.exception.EmptyRefreshTokenException;
import com.blocker.blocker_server.user.exception.UserNotFoundException;
import com.blocker.blocker_server.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

class UserServiceSupportTest extends IntegrationTestSupport {

    @Autowired
    private UserServiceSupport userServiceSupport;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("email로 User를 찾는다.")
    @Test
    void getUserByEmail() {

        /** given */

        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);

        /** when */

        User result = userServiceSupport.getUserByEmail("testEmail");

        /** then */

        assertThat(result).isNotNull();

    }

    @DisplayName("email로 User를 찾는데 없으면 UserNotFoundException을 던진다.")
    @Test
    void getUserByEmailException() {

        /** given */

        /** when then */
        assertThatThrownBy(() -> userServiceSupport.getUserByEmail("testEmail"))
                .isInstanceOf(UserNotFoundException.class);


    }

    @DisplayName("email 또는 name에 keyword가 포함된 User를 찾는다.")
    @Test
    void searchUsers() {

        /** given */
        User user1 = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("222", "222222", "2222222222", "2222222", List.of("USER"));
        User user3 = User.create("333", "testName3", "3333333333", "333333333", List.of("USER"));

        userRepository.saveAll(List.of(user1, user2, user3));

        /** when */

        List<User> result = userServiceSupport.searchUsers("test");

        /** then */

        assertThat(result).hasSize(2);

        List<String> emails = result.stream()
                .map(user -> user.getEmail())
                .collect(Collectors.toList());
        assertThat(emails)
                .contains("testEmail")
                .contains("333");

    }

    @DisplayName("User 리스트를 SearchUserResponse 리스트로 만든다.")
    @Test
    void createSearchUserResponse() {

        /** given */

        User user1 = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));

        userRepository.saveAll(List.of(user1, user2, user3));

        /** when */

        List<SearchUserResponse> result = userServiceSupport.createSearchUserResponse(List.of(user1, user2, user3));

        /** then */

        assertThat(result).hasSize(3);

        List<String> emails = result.stream()
                .map(user -> user.getEmail())
                .collect(Collectors.toList());
        assertThat(emails)
                .contains(user1.getEmail())
                .contains(user2.getEmail())
                .contains(user3.getEmail());

    }

    @DisplayName("refresh token value로 User를 찾는다.")
    @Test
    void getUserByRefreshTokenValue() {

        /** given */

        User user1 = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user1);

        String token = jwtProvider.createRefreshToken("testValue");
        /** when */

        User result = userServiceSupport.getUserByRefreshTokenValue(token);

        /** then */
        assertThat(result.getEmail()).isEqualTo(user1.getEmail());

    }

    @DisplayName("")
    @Test
    void createHeadersWithJwt() {

        /** given */
        User user1 = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user1);

        /** when */

        HttpHeaders result = userServiceSupport.createHeadersWithJwt(user1.getEmail(), user1.getName(), user1.getRefreshtokenValue(), user1.getRoles());

        /** then */

        assertThat(result.containsKey("Authorization")).isTrue();
        assertThat(result.containsKey("COOKIE")).isTrue();

    }

    @DisplayName("random uuid로 랜덤키를 생성하고 '-'를 제거한다.")
    @Test
    void getNewRefreshTokenValue() {

        /** given */


        /** when */
        String result = userServiceSupport.getNewRefreshTokenValue();

        /** then */

        assertThat(!result.contains("-")).isTrue();
    }

    @DisplayName("refresh token이 null 혹은 공백이면 EmptyRefreshTokenException을 던진다.")
    @Test
    void checkIsEmptyRefreshToken() {

        /** given */

        /** when then */

        assertThatThrownBy(() -> userServiceSupport.checkIsEmptyRefreshToken(""))
                .isInstanceOf(EmptyRefreshTokenException.class);
    }



}