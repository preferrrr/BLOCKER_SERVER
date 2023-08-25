package com.blocker.blocker_server.controller;

import com.blocker.blocker_server.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.dto.response.GetBoardResponseDto;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**메인페이지 게시글 리스트
     * /boards
     * */
    @GetMapping("")
    public ResponseEntity<List<GetBoardListResponseDto>> getBoards(@PageableDefault(size = 4, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        List<GetBoardListResponseDto> response = boardService.getBoards(pageable);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 게시글 조회
     * /boards/{boardId}*/
    @GetMapping("/{boardId}")
    public ResponseEntity<GetBoardResponseDto> getBoard(@AuthenticationPrincipal User user, @PathVariable("boardId") Long boardId) {

        GetBoardResponseDto response = boardService.getBoard(user, boardId);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
