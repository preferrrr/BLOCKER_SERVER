package com.blocker.blocker_server.board.service;

import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.board.domain.Image;
import com.blocker.blocker_server.board.dto.request.ModifyBoardRequestDto;
import com.blocker.blocker_server.board.dto.request.SaveBoardRequestDto;
import com.blocker.blocker_server.board.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.board.dto.response.GetBoardResponseDto;
import com.blocker.blocker_server.board.dto.response.ImageDto;
import com.blocker.blocker_server.board.repository.BoardRepository;
import com.blocker.blocker_server.board.repository.ImageRepository;
import com.blocker.blocker_server.bookmark.repository.BookmarkRepository;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.commons.exception.ForbiddenException;
import com.blocker.blocker_server.commons.exception.NotFoundException;
import com.blocker.blocker_server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final ContractRepository contractRepository;

    public List<GetBoardListResponseDto> getBoards(Pageable pageable) {

        List<Board> entityList = boardRepository.getBoardList(pageable);

        return createDtos(entityList);
    }

    public List<GetBoardListResponseDto> createDtos(List<Board> entityList) {

        return entityList.stream()
                .map(board -> GetBoardListResponseDto.of(board))
                .collect(Collectors.toList());
    }

    public GetBoardResponseDto getBoard(User me, Long boardId) {
        Board board = boardRepository.getBoardWithImages(boardId).orElseThrow(() -> new NotFoundException("[get board] boardId : " + boardId));

        boolean isWriter = board.getUser().getEmail().equals(me.getEmail());
        boolean isBookmark = bookmarkRepository.existsByUserAndBoard(me, board);

        List<ImageDto> imageAddresses = board.getImages().stream()
                .map(image -> ImageDto.of(image))
                .collect(Collectors.toList());

        return GetBoardResponseDto.of(board, imageAddresses, isWriter, isBookmark);

    }

    @Transactional
    public void saveBoard(User user, SaveBoardRequestDto requestDto) {

        User me = userRepository.getReferenceById(user.getEmail());

        Contract contract = contractRepository.findById(requestDto.getContractId()).orElseThrow(() -> new NotFoundException("[modify board] contractId : " + requestDto.getContractId()));
        if (!contract.getUser().getEmail().equals(me.getEmail()))
            throw new ForbiddenException("[save board] contractId, email : " + contract.getContractId() + ", " + me.getEmail());

        Board newBoard = Board.create(me, requestDto.getTitle(), requestDto.getContent(), requestDto.getInfo(), requestDto.getRepresentImage(), contract);

        List<Image> images = requestDto.getImages().stream()
                .map(imageAddress -> Image.of(newBoard, imageAddress))
                .collect(Collectors.toList());

        boardRepository.save(newBoard);
        imageRepository.saveAll(images);

    }

    @Transactional
    public void deleteBoard(User me, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException("[delete board] boardId : " + boardId));

        if (!board.getUser().getEmail().equals(me.getEmail()))
            throw new ForbiddenException("[delete board] boardId, email : " + boardId + ", " + me.getEmail());

        boardRepository.deleteById(boardId);
    }

    @Transactional
    public void modifyBoard(User me, Long boardId, ModifyBoardRequestDto requestDto) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException("[modify board] boardId : " + boardId));

        if (!board.getUser().getEmail().equals(me.getEmail()))
            throw new ForbiddenException("[modify board] boardId, email : " + boardId + ", " + me.getEmail());

        Contract contract = modifyContractBelongingToBoard(me, board, requestDto.getContractId());

        //Dynamic insert로 바뀐 값만 업데이트 됨.
        board.updateBoard(requestDto.getTitle(), requestDto.getContent(), requestDto.getInfo(), requestDto.getRepresentImage(), contract);

        List<Image> addImages = requestDto.getAddImageAddresses().stream()
                .map(imageAddress -> Image.of(board, imageAddress))
                .collect(Collectors.toList());

        imageRepository.saveAll(addImages);
        imageRepository.deleteAllById(requestDto.getDeleteImageIds()); //imageId가 존재하는지 db 조회로 검증을 해야할까 ?

    }

    private Contract modifyContractBelongingToBoard(User user, Board board, Long modifyContractId) {

        //계약서가 달라졌으면 조회 후 나의 계약서가 맞는지 확인
        if (!board.getContract().getContractId().equals(modifyContractId)) {
            Contract contract = contractRepository.findById(modifyContractId).orElseThrow(() -> new NotFoundException("[modify board] contractId : " + modifyContractId));

            if (!contract.getUser().getEmail().equals(board.getUser().getEmail()))
                throw new ForbiddenException("[modify board] contractId, email : " + contract.getContractId() + ", " + user.getEmail());

            return contract;
        }

        return contractRepository.getReferenceById(modifyContractId);
    }

    public List<GetBoardListResponseDto> getMyBoards(User me, Pageable pageable) {

        List<Board> entityList = boardRepository.getMyBoards(me, pageable);

        List<GetBoardListResponseDto> response = createDtos(entityList);

        return response;
    }
}
