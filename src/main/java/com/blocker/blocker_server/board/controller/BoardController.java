package com.blocker.blocker_server.board.controller;

import com.blocker.blocker_server.board.service.BoardService;
import com.blocker.blocker_server.board.dto.request.ModifyBoardRequestDto;
import com.blocker.blocker_server.board.dto.request.SaveBoardRequestDto;
import com.blocker.blocker_server.board.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.board.dto.response.GetBoardResponseDto;
import com.blocker.blocker_server.commons.response.BaseResponse;
import com.blocker.blocker_server.commons.response.ListResponse;
import com.blocker.blocker_server.commons.response.SingleResponse;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<ListResponse<GetBoardListResponseDto>> getBoards(@PageableDefault(size = 4, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(
                ListResponse.ok(boardService.getBoards(pageable))
        );
    }

    /**
     * 게시글 조회
     * /boards/{boardId}
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<SingleResponse<GetBoardResponseDto>> getBoard(@PathVariable("boardId") Long boardId) {

        return ResponseEntity.ok(
                SingleResponse.ok(boardService.getBoard(boardId))
        );

    }

    /**
     * 게시글 작성
     * /boards
     */
    @PostMapping("")
    public ResponseEntity<BaseResponse> saveBoard(@RequestBody SaveBoardRequestDto requestDto) {

        boardService.saveBoard(requestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.of(HttpStatus.CREATED)) ;
    }


    /**
     * 게시글 삭제
     * /boards
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<BaseResponse> deleteBoard(@PathVariable("boardId") Long boardId) {

        boardService.deleteBoard(boardId);

        return ResponseEntity.ok(BaseResponse.ok());
    }

    /**
     * 게시글 수정
     * /boards/{boardId}
     */
    @PatchMapping("/{boardId}")
    public ResponseEntity<BaseResponse> modifyBoard(@PathVariable("boardId") Long boardId,
                                                  @RequestBody ModifyBoardRequestDto requestDto) {

        boardService.modifyBoard(boardId, requestDto);

        return ResponseEntity.ok(BaseResponse.ok());

    }

    /**
     * 내가 쓴 게시글 조회
     * /boards/my-boards
     */
    @GetMapping("/my-boards")
    public ResponseEntity<ListResponse<GetBoardListResponseDto>> getMyBoards(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(
                ListResponse.ok(boardService.getMyBoards(pageable))
        );
    }
}
