package com.blocker.blocker_server.service;

import com.blocker.blocker_server.dto.request.SaveBoardRequestDto;
import com.blocker.blocker_server.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.dto.response.GetBoardResponseDto;
import com.blocker.blocker_server.dto.response.ImageDto;
import com.blocker.blocker_server.entity.Board;
import com.blocker.blocker_server.entity.Image;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.exception.ForbiddenException;
import com.blocker.blocker_server.exception.NotFoundException;
import com.blocker.blocker_server.repository.BoardRepository;
import com.blocker.blocker_server.repository.BookmarkRepository;
import com.blocker.blocker_server.repository.ImageRepository;
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
    private final ImageRepository imageRepository;

    public List<GetBoardListResponseDto> getBoards(Pageable pageable) {

        List<Board> entityList = boardRepository.getBoardList(pageable);

        List<GetBoardListResponseDto> response = createDtos(entityList);

        return response;
    }

    public List<GetBoardListResponseDto> createDtos(List<Board> entityList) {

        List<GetBoardListResponseDto> dtos = new ArrayList<>();

        for(Board board : entityList) {
            GetBoardListResponseDto dto = GetBoardListResponseDto.builder()
                    .boardId(board.getBoardId())
                    .createdAt(board.getCreatedAt())
                    .modifiedAt(board.getModifiedAt())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .name(board.getUser().getName())
                    .view(board.getView())
                    .bookmarkCount(board.getBookmarkCount())
                    .representImage(board.getRepresentImage())
                    .build();

            dtos.add(dto);
        }

        return dtos;
    }

    public GetBoardResponseDto getBoard(User me, Long boardId) {
        Board board = boardRepository.getBoard(boardId).orElseThrow(()->new NotFoundException("[get board] boardId : " + boardId));

        boolean isWriter = board.getUser().getEmail().equals(me.getEmail());
        boolean isBookmark = bookmarkRepository.existsByUserAndBoard(me, board);

        List<ImageDto> imageAddresses = new ArrayList<>();
        for(Image image : board.getImages()) {

            ImageDto imageDto = ImageDto.builder()
                    .imageId(image.getImageId())
                    .imageAddress(image.getImageAddress())
                    .build();

            imageAddresses.add(imageDto);
        }

        GetBoardResponseDto response = GetBoardResponseDto.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .name(board.getUser().getName())
                .content(board.getContent())
                .representImage(board.getRepresentImage())
                .view(board.getView())
                .bookmarkCount(board.getBookmarkCount())
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .images(imageAddresses)
                .info(board.getInfo())
                .contractId(null)
                .isWriter(isWriter)
                .isBookmark(isBookmark)
                .build();

        return response;

    }

    public void saveBoard(User me, SaveBoardRequestDto requestDto) {

        //TODO : 계약서
        Board newBoard = Board.builder()
                .user(me)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .info(requestDto.getInfo())
                .representImage(requestDto.getRepresentImage())
                .build();

        List<Image> images = new ArrayList<>();

        for(String imageAddress : requestDto.getImages()) {
            Image newImage = Image.builder()
                    .board(newBoard)
                    .imageAddress(imageAddress)
                    .build();
            images.add(newImage);
        }
        boardRepository.save(newBoard);
        imageRepository.saveAll(images);

    }

    public void deleteBoard(User me, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(()->new NotFoundException("[delete board] boardId : " + boardId));

        if(!board.getUser().getEmail().equals(me.getEmail()))
            throw new ForbiddenException("[delete board] boardId, email : " + boardId + ", " + me.getEmail());

        boardRepository.deleteById(boardId);
    }
}
