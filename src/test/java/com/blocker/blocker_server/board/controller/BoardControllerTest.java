package com.blocker.blocker_server.board.controller;

import com.blocker.blocker_server.board.dto.request.ModifyBoardRequestDto;
import com.blocker.blocker_server.board.dto.request.SaveBoardRequestDto;
import com.blocker.blocker_server.board.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.board.dto.response.GetBoardResponseDto;
import com.blocker.blocker_server.board.service.BoardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BoardController.class)
@WithMockUser(roles = "USER")
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardService boardService;


    @DisplayName("게시글 리스트를 불러온다.")
    @Test
    void getBoards() throws Exception {

        /** given */

        given(boardService.getBoards(any(Pageable.class))).willReturn(List.of());

        /** when */

        /** then */

        mockMvc.perform(get("/boards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(boardService, times(1)).getBoards(any(Pageable.class));
    }

    @DisplayName("게시글 인덱스로 게시글을 조회한다.")
    @Test
    void getBoardById() throws Exception {

        /** given */
        GetBoardResponseDto response = GetBoardResponseDto.builder()
                .boardId(1l)
                .title("testTitle")
                .content("testContent")
                .bookmarkCount(0)
                .view(0)
                .isBookmark(true)
                .isWriter(true)
                .modifiedAt(LocalDateTime.of(2024, 1, 1, 12, 0))
                .createdAt(LocalDateTime.of(2024, 1, 1, 12, 0))
                .contractId(1l)
                .representImage("testImage")
                .images(List.of())
                .name("testName")
                .info("testInfo")
                .build();

        given(boardService.getBoard(any(), anyLong())).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/boards/{boardId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardId").exists())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.representImage").isNotEmpty())
                .andExpect(jsonPath("$.view").isNotEmpty())
                .andExpect(jsonPath("$.bookmarkCount").isNotEmpty())
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.modifiedAt").isNotEmpty())
                .andExpect(jsonPath("$.images").isArray())
                .andExpect(jsonPath("$.info").isNotEmpty())
                .andExpect(jsonPath("$.contractId").isNotEmpty())
                .andExpect(jsonPath("$.isWriter").isNotEmpty())
                .andExpect(jsonPath("$.isBookmark").isNotEmpty());

        verify(boardService, times(1)).getBoard(any(), anyLong());
    }

    @DisplayName("게시글을 작성한다.")
    @Test
    void saveBoard() throws Exception {

        /** given */

        SaveBoardRequestDto request = SaveBoardRequestDto.builder()
                .title("testTitle")
                .content("testContent")
                .info("testInfo")
                .representImage("testImage")
                .images(List.of("testImage1", "testImage2"))
                .contractId(1l)
                .build();

        willDoNothing().given(boardService).saveBoard(any(), any(SaveBoardRequestDto.class));

        /** when */

        /** then */

        mockMvc.perform(post("/boards")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(boardService, times(1)).saveBoard(any(), any(SaveBoardRequestDto.class));

    }

    @DisplayName("게시글 작성 request dto 중 null이 있으면 InvalidRequestParameterException을 반환한다.")
    @Test
    void saveBoardInvalidField() throws Exception {

        /** given */

        SaveBoardRequestDto request = SaveBoardRequestDto.builder()
                .title(null)
                .content("testContent")
                .info("testInfo")
                .representImage("testImage")
                .images(List.of("testImage1", "testImage2"))
                .contractId(1l)
                .build();

        willDoNothing().given(boardService).saveBoard(any(), any(SaveBoardRequestDto.class));

        /** when */

        /** then */

        mockMvc.perform(post("/boards")
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @DisplayName("게시글을 수정한다.")
    @Test
    void modifyBoard() throws Exception {

        /** given */

        ModifyBoardRequestDto request = ModifyBoardRequestDto.builder()
                .content("test")
                .title("test")
                .addImageAddresses(List.of("test1", "test2"))
                .deleteImageIds(List.of(1l, 2l))
                .info("test")
                .contractId(1l)
                .representImage("test")
                .build();

        willDoNothing().given(boardService).modifyBoard(any(), anyLong(), any(ModifyBoardRequestDto.class));

        /** when */


        /** then */

        mockMvc.perform(patch("/boards/{boardId}", 1l)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(boardService, times(1)).modifyBoard(any(), anyLong(), any(ModifyBoardRequestDto.class));

    }

    @DisplayName("게시글 수정 request dto 중 filed가 null이면 InvalidRequestParameterException (204)을 반환한다.")
    @Test
    void modifyBoardInvalidFields() throws Exception {

        /** given */

        ModifyBoardRequestDto request = ModifyBoardRequestDto.builder()
                .content(null)
                .title("test")
                .addImageAddresses(List.of("test1", "test2"))
                .deleteImageIds(List.of(1l, 2l))
                .info("test")
                .contractId(1l)
                .representImage("test")
                .build();

        willDoNothing().given(boardService).modifyBoard(any(), anyLong(), any(ModifyBoardRequestDto.class));

        /** when */


        /** then */

        mockMvc.perform(patch("/boards/{boardId}", 1l)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @DisplayName("게시글을 삭제한다.")
    @Test
    void deleteBoard() throws Exception {

        /** given */

        /** when */

        willDoNothing().given(boardService).deleteBoard(any(), anyLong());

        /** then */

        mockMvc.perform(delete("/boards/{boardId}", 1l)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(boardService, times(1)).deleteBoard(any(), anyLong());

    }

    @DisplayName("내가 쓴 게시글 리스트를 조회한다.")
    @Test
    void getMyBoards() throws Exception {

        /** given */

        List<GetBoardListResponseDto> response = List.of();

        given(boardService.getMyBoards(any(), any(Pageable.class))).willReturn(response);

        /** when */

        /** then */

        mockMvc.perform(get("/boards/my-boards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(boardService, times(1)).getMyBoards(any(), any(Pageable.class));

    }

}