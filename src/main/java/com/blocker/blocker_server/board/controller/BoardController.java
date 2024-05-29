package com.blocker.blocker_server.board.controller;

import com.blocker.blocker_server.board.service.BoardService;
import com.blocker.blocker_server.board.dto.request.ModifyBoardRequestDto;
import com.blocker.blocker_server.board.dto.request.SaveBoardRequestDto;
import com.blocker.blocker_server.board.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.board.dto.response.GetBoardResponseDto;
import com.blocker.blocker_server.commons.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static com.blocker.blocker_server.commons.response.response_code.BoardResponseCode.*;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 메인페이지 게시글 리스트
     * /boards
     */
    @GetMapping("")
    public ApiResponse<GetBoardListResponseDto> getBoards(@PageableDefault(size = 4, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ApiResponse.of(
                boardService.getBoards(pageable),
                GET_MY_BOARD_LIST
        );
    }

    /**
     * 게시글 조회
     * /boards/{boardId}
     */
    @GetMapping("/{boardId}")
    public ApiResponse<GetBoardResponseDto> getBoard(@PathVariable("boardId") Long boardId) {

        return ApiResponse.of(
                boardService.getBoard(boardId),
                GET_BOARD
        );

    }

    /**
     * 게시글 작성
     * /boards
     */
    @PostMapping("")
    public ApiResponse saveBoard(@RequestBody @Valid SaveBoardRequestDto requestDto) {

        boardService.saveBoard(requestDto);

        return ApiResponse.of(POST_BOARD);
    }


    /**
     * 게시글 삭제
     * /boards
     */
    @DeleteMapping("/{boardId}")
    public ApiResponse deleteBoard(@PathVariable("boardId") Long boardId) {

        boardService.deleteBoard(boardId);

        return ApiResponse.of(DELETE_BOARD);
    }

    /**
     * 게시글 수정
     * /boards/{boardId}
     */
    @PatchMapping("/{boardId}")
    public ApiResponse modifyBoard(@PathVariable("boardId") Long boardId,
                                   @RequestBody @Valid ModifyBoardRequestDto requestDto) {

        boardService.modifyBoard(boardId, requestDto);

        return ApiResponse.of(MODIFY_BOARD);

    }

    /**
     * 내가 쓴 게시글 조회
     * /boards/my-boards
     */
    @GetMapping("/my-boards")
    public ApiResponse<GetBoardListResponseDto> getMyBoards(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ApiResponse.of(
                boardService.getMyBoards(pageable),
                GET_MY_BOARD_LIST
        );
    }
}
