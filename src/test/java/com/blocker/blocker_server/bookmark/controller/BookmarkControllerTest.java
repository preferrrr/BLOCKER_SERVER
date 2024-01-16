package com.blocker.blocker_server.bookmark.controller;

import com.blocker.blocker_server.ControllerTestSupport;
import com.blocker.blocker_server.board.dto.response.GetBoardListResponseDto;
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

        willDoNothing().given(bookmarkService).saveBookmark(any(), any(SaveBookmarkRequestDto.class));

        /** when */

        /** then */

        mockMvc.perform(post("/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(bookmarkService, timeout(1)).saveBookmark(any(), any(SaveBookmarkRequestDto.class));

    }

    @DisplayName("북마크 등록 request dto field가 null이면 InvalidRequestParameterException (204)을 반환한다.")
    @Test
    void saveBookmarkInvalidField() throws Exception {

        /** given */
        SaveBookmarkRequestDto request = SaveBookmarkRequestDto.builder()
                .boardId(null)
                .build();

        willDoNothing().given(bookmarkService).saveBookmark(any(), any(SaveBookmarkRequestDto.class));

        /** when */

        /** then */

        mockMvc.perform(post("/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isNoContent());

    }

    @DisplayName("게시글 북마크를 해제한다.")
    @Test
    void deleteBookmark() throws Exception {

        /** given */

        willDoNothing().given(bookmarkService).deleteBookmark(any(), anyLong());

        /** when */

        /** then */

        mockMvc.perform(delete("/bookmarks/{boardId}", 1l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(bookmarkService, timeout(1)).deleteBookmark(any(), anyLong());

    }

    @DisplayName("북마크로 등록한 게시글을 조회한다.")
    @Test
    void getBookmarkBoards() throws Exception {

        /** given */

        List<GetBoardListResponseDto> response = List.of();
        given(bookmarkService.getBookmarkBoards(any(), any(Pageable.class))).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/bookmarks/boards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(bookmarkService, timeout(1)).getBookmarkBoards(any(), any(Pageable.class));

    }

}