package com.blocker.blocker_server.service;

import com.blocker.blocker_server.dto.response.GetBoardsResponseDto;
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

    public List<GetBoardsResponseDto> getBoards(Pageable pageable) {

        List<GetBoardsResponseDto> response = new ArrayList<>();



        return response;
    }
}
