package com.blocker.blocker_server.bookmark.controller;

import com.blocker.blocker_server.ControllerTestSupport;
import com.blocker.blocker_server.bookmark.dto.request.SaveBookmarkRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(roles = "USER")
class BookmarkControllerTest extends ControllerTestSupport {

    @DisplayName("게시글을 북마크로 등록한다.")
    @Test
    void saveBookmark() throws Exception {

        /** given */
        SaveBookmarkRequestDto request = SaveBookmarkRequestDto.builder()
                .boardId(1l)
                .build();

        willDoNothing().given(bookmarkService).saveBookmark(any(SaveBookmarkRequestDto.class));

        /** when */

        /** then */

        mockMvc.perform(post("/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"));

        verify(bookmarkService, times(1)).saveBookmark(any(SaveBookmarkRequestDto.class));

    }

    @DisplayName("북마크 등록 request dto field가 null이면 InvalidRequestParameterException (204)을 반환한다.")
    @Test
    void saveBookmarkInvalidField() throws Exception {

        /** given */
        SaveBookmarkRequestDto request = SaveBookmarkRequestDto.builder()
                .boardId(null)
                .build();

        /** when */

        /** then */

        mockMvc.perform(post("/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("게시글 인덱스는 null 또는 공백일 수 없습니다."));

        verify(bookmarkService, times(0)).saveBookmark(any(SaveBookmarkRequestDto.class));

    }

    @DisplayName("게시글 북마크를 해제한다.")
    @Test
    void deleteBookmark() throws Exception {

        /** given */

        willDoNothing().given(bookmarkService).deleteBookmark(anyLong());

        /** when */

        /** then */

        mockMvc.perform(delete("/bookmarks/{boardId}", 1l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"));

        verify(bookmarkService, times(1)).deleteBookmark(anyLong());

    }

    @DisplayName("북마크로 등록한 게시글을 조회한다.")
    @Test
    void getBookmarkBoards() throws Exception {

        /** given */

        given(bookmarkService.getBookmarkBoards(any(Pageable.class))).willReturn(List.of());

        /** when */

        /** then */

        mockMvc.perform(get("/bookmarks/boards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isArray());

        verify(bookmarkService, times(1)).getBookmarkBoards(any(Pageable.class));

    }

}