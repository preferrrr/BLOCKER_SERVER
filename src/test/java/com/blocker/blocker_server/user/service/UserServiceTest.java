package com.blocker.blocker_server.user.service;

import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.dto.response.SearchUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserServiceSupport userServiceSupport;

    private User user;
    @BeforeEach
    void setUp() {
        user = User.create("testEmail","testName","testPicture","testValue",List.of("USER"));
    }

    @DisplayName("JWT를 재발급하고, 헤더에 실어서 반환한다.")
    @Test
    void reissueToken() {

        /** given */
        willDoNothing().given(userServiceSupport).checkIsEmptyRefreshToken(anyString());
        given(userServiceSupport.getRefreshTokenFromCookie(anyString())).willReturn("testValue");
        given(userServiceSupport.getUserByRefreshTokenValue(anyString())).willReturn(user);
        given(userServiceSupport.getNewRefreshTokenValue()).willReturn("newValue");
        given(userServiceSupport.createHeadersWithJwt(anyString(),anyString(),anyString(),anyList())).willReturn(mock(HttpHeaders.class));

        /** when */

        HttpHeaders result = userService.reissueToken("cookie");

        /** then */

        verify(userServiceSupport, times(1)).checkIsEmptyRefreshToken(anyString());
        verify(userServiceSupport, times(1)).getRefreshTokenFromCookie(anyString());
        verify(userServiceSupport, times(1)).getUserByRefreshTokenValue(anyString());
        verify(userServiceSupport, times(1)).createHeadersWithJwt(anyString(),anyString(),anyString(),anyList());

        assertThat(user.getRefreshtokenValue()).isEqualTo("newValue");
    }

    @DisplayName("이메일 또는 닉네임에 keyword가 포함된 사용자를 검색해서 반환한다.")
    @Test
    void searchUsers() {

        /** given */

        given(userServiceSupport.searchUsers(anyString())).willReturn(mock(List.class));
        given(userServiceSupport.createSearchUserResponse(anyList())).willReturn(mock(List.class));

        /** when */

        List<SearchUserResponse> result = userService.searchUsers("keyword");

        /** then */

        verify(userServiceSupport, times(1)).searchUsers(anyString());
        verify(userServiceSupport, times(1)).createSearchUserResponse(anyList());


    }

}