package com.blocker.blocker_server.board.controller;

import com.blocker.blocker_server.board.service.BoardService;
import com.blocker.blocker_server.board.dto.request.ModifyBoardRequestDto;
import com.blocker.blocker_server.board.dto.request.SaveBoardRequestDto;
import com.blocker.blocker_server.board.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.board.dto.response.GetBoardResponseDto;
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
    public ResponseEntity<List<GetBoardListResponseDto>> getBoards(@PageableDefault(size = 4, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return new ResponseEntity<>(
                boardService.getBoards(pageable),
                HttpStatus.OK
        );
    }

    /**
     * 게시글 조회
     * /boards/{boardId}
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<GetBoardResponseDto> getBoard(@AuthenticationPrincipal User user,
                                                        @PathVariable("boardId") Long boardId) {

        return new ResponseEntity<>(
                boardService.getBoard(user, boardId),
                HttpStatus.OK
        );

    }

    /**
     * 게시글 작성
     * /boards
     */
    @PostMapping("")
    public ResponseEntity<HttpStatus> saveBoard(@AuthenticationPrincipal User user,
                                                @RequestBody SaveBoardRequestDto requestDto) {

        boardService.saveBoard(user, requestDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /**
     * 게시글 삭제
     * /boards
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<HttpStatus> deleteBoard(@AuthenticationPrincipal User user,
                                                  @PathVariable("boardId") Long boardId) {

        boardService.deleteBoard(user, boardId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 게시글 수정
     * /boards/{boardId}
     */
    @PatchMapping("/{boardId}")
    public ResponseEntity<HttpStatus> modifyBoard(@AuthenticationPrincipal User user,
                                                  @PathVariable("boardId") Long boardId,
                                                  @RequestBody ModifyBoardRequestDto requestDto) {

        boardService.modifyBoard(user, boardId, requestDto);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * 내가 쓴 게시글 조회
     * /boards/my-boards
     */
    @GetMapping("/my-boards")
    public ResponseEntity<List<GetBoardListResponseDto>> getMyBoards(@AuthenticationPrincipal User user,
                                                                     @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return new ResponseEntity<>(
                boardService.getMyBoards(user, pageable),
                HttpStatus.OK
        );
    }
}
