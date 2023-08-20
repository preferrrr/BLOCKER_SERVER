package com.blocker.blocker_server.controller;

import com.blocker.blocker_server.dto.response.GetBoardsResponseDto;
import com.blocker.blocker_server.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<?> getBoards(@PageableDefault(size = 4, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        List<GetBoardsResponseDto> response = boardService.getBoards(pageable);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
