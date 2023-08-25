package com.blocker.blocker_server.service;

import com.blocker.blocker_server.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.entity.Board;
import com.blocker.blocker_server.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public List<GetBoardListResponseDto> getBoards(Pageable pageable) {

        List<Board> entityList = boardRepository.getBoards(pageable);

        List<GetBoardListResponseDto> response = createDtos(entityList);

        return response;
    }

    public List<GetBoardListResponseDto> createDtos(List<Board> entityList) {

        List<GetBoardListResponseDto> dtos = new ArrayList<>();

        for(Board board : entityList) {
            GetBoardListResponseDto dto = new GetBoardListResponseDto();
            dto.setBoardId(board.getBoardId());
            dto.setCreatedAt(board.getCreatedAt());
            dto.setModifiedAt(board.getModifiedAt());
            dto.setTitle(board.getTitle());
            dto.setContent(board.getContent());
            dto.setName(board.getUser().getName());
            dto.setView(board.getView());
            dto.setBookmarkCount(board.getBookmarkCount());
            dto.setRepresentImage("test image");

            dtos.add(dto);
        }

        return dtos;
    }
}
