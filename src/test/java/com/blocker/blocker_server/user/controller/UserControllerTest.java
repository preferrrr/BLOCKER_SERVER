package com.blocker.blocker_server.user.controller;

import com.blocker.blocker_server.ControllerTestSupport;
import com.blocker.blocker_server.user.dto.response.SearchUserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTestSupport {

    @DisplayName("엑세스 토큰을 재발급한다.")
    @Test
    @WithMockUser(roles = "USER")
    void reissueToken() throws Exception {

        /** given */
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer accessToken");
        headers.add(HttpHeaders.COOKIE, "refresh token");

        given(userService.reissueToken(anyString())).willReturn(headers);

        /** when */

        /** then */

        mockMvc.perform(get("/users/reissue-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.COOKIE, "refresh token"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists("Authorization"))
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.COOKIE));

        verify(userService, times(1)).reissueToken(anyString());

    }


    @DisplayName("엑세스 토큰을 재발급할 때 리프레시 토큰 쿠키를 보내지 않으면 204을 반환한다.")
    @Test
    @WithMockUser(roles = "USER")
    void reissueTokenEmptyCookie() throws Exception {

        /** given */

        /** when */

        /** then */

        mockMvc.perform(get("/users/reissue-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.COOKIE, ""))
                .andExpect(status().isNoContent());


    }

    @DisplayName("email이나 username에 keyword가 포함된 유저를 찾아 반환한다.")
    @Test
    @WithMockUser(roles = "USER")
    void searchUsers() throws Exception {

        /** given */

        List<SearchUserResponse> response = List.of();
        given(userService.searchUsers(anyString())).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/users/search")
                        .param("keyword","test keyword")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(userService, times(1)).searchUsers(anyString());

    }
}