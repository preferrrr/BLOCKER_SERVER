package com.blocker.blocker_server.service;

import com.blocker.blocker_server.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.dto.response.GetBoardResponseDto;
import com.blocker.blocker_server.dto.response.ImageDto;
import com.blocker.blocker_server.entity.Board;
import com.blocker.blocker_server.entity.Image;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.exception.NotFoundException;
import com.blocker.blocker_server.repository.BoardRepository;
import com.blocker.blocker_server.repository.BookmarkRepository;
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
    private final BookmarkRepository bookmarkRepository;

    public List<GetBoardListResponseDto> getBoards(Pageable pageable) {

        List<Board> entityList = boardRepository.getBoardList(pageable);

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

    public GetBoardResponseDto getBoard(User me, Long boardId) {
        Board board = boardRepository.getBoard(boardId).orElseThrow(()->new NotFoundException("[get board] boardId : " + boardId));

        boolean isWriter = board.getUser().getEmail().equals(me.getEmail());
        boolean isBookmark = bookmarkRepository.existsByUserAndBoard(me, board);

        GetBoardResponseDto response = new GetBoardResponseDto();

        response.setBoardId(board.getBoardId());
        response.setTitle(board.getTitle());
        response.setName(board.getUser().getName());
        response.setRepresentImage(board.getRepresentImage());
        response.setView(board.getView());
        response.setBookmarkCount(board.getBookmarkCount());
        response.setCreatedAt(board.getCreatedAt());
        response.setModifiedAt(board.getModifiedAt());

        List<ImageDto> imageAddresses = new ArrayList<>();
        for(Image image : board.getImages()) {
            ImageDto imageDto = new ImageDto();
            imageDto.setImageId(image.getImageId());
            imageDto.setImageAddress(image.getImageAddress());
            imageAddresses.add(imageDto);
        }

        response.setInfo(board.getInfo());
        response.setContractId(null);
        response.setWriter(isWriter);
        response.setBookmark(isBookmark);

        return response;

    }
}
