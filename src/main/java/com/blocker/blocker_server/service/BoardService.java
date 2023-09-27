package com.blocker.blocker_server.service;

import com.blocker.blocker_server.dto.request.ModifyBoardRequestDto;
import com.blocker.blocker_server.dto.request.SaveBoardRequestDto;
import com.blocker.blocker_server.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.dto.response.GetBoardResponseDto;
import com.blocker.blocker_server.dto.response.ImageDto;
import com.blocker.blocker_server.entity.Board;
import com.blocker.blocker_server.entity.Contract;
import com.blocker.blocker_server.entity.Image;
import com.blocker.blocker_server.entity.User;
import com.blocker.blocker_server.exception.DuplicateContractException;
import com.blocker.blocker_server.exception.ForbiddenException;
import com.blocker.blocker_server.exception.NotFoundException;
import com.blocker.blocker_server.repository.*;
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
    private final UserRepository userRepository;
    private final ContractRepository contractRepository;

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
                .contractId(board.getContract().getContractId())
                .isWriter(isWriter)
                .isBookmark(isBookmark)
                .build();

        return response;

    }

    public void saveBoard(User user, SaveBoardRequestDto requestDto) {

        User me = userRepository.getReferenceById(user.getEmail());

        Contract contract = contractRepository.findById(requestDto.getContractId()).orElseThrow(()-> new NotFoundException("[modify board] contractId : " + requestDto.getContractId()));
        if(!contract.getUser().getEmail().equals(me.getEmail()))
            throw new ForbiddenException("[save board] contractId, email : " + contract.getContractId() + ", " + me.getEmail());
        //해당 계약서로 이미 게시글 작성했으면 409 응답.
        if(boardRepository.existsByContract(contract))
            throw new DuplicateContractException("contractId : " + contract.getContractId());

        Board newBoard = Board.builder()
                .user(me)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .info(requestDto.getInfo())
                .representImage(requestDto.getRepresentImage())
                .contract(contract)
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

    public void modifyBoard(User me, Long boardId, ModifyBoardRequestDto requestDto) {
        Board board = boardRepository.findById(boardId).orElseThrow(()->new NotFoundException("[modify board] boardId : " + boardId));

        if(!board.getUser().getEmail().equals(me.getEmail()))
            throw new ForbiddenException("[modify board] boardId, email : " + boardId + ", " + me.getEmail());

        Contract contract;
        //게시글에 포함된 계약서가 달라졌다면, 새로운 계약서 조회해서 나의 계약서가 맞는지 체크. 같다면 확인해줄 필요 없음
        if(!board.getContract().getContractId().equals(requestDto.getContractId())) {
            contract = contractRepository.findById(requestDto.getContractId()).orElseThrow(()-> new NotFoundException("[modify board] contractId : " + requestDto.getContractId()));

            if(!contract.getUser().getEmail().equals(me.getEmail()))
                throw new ForbiddenException("[modify board] contractId, email : " + contract.getContractId() + ", " + me.getEmail());

        } else
            contract = contractRepository.getReferenceById(requestDto.getContractId());
        //Dynamic insert로 바뀐 값만 업데이트 됨.
        board.updateBoard(requestDto.getTitle(), requestDto.getContent(), requestDto.getInfo(), requestDto.getRepresentImage(), contract);

        List<Image> addImages = new ArrayList<>();
        for(String imageAddress : requestDto.getAddImageAddresses()) {
            Image image = Image.builder()
                    .board(board)
                    .imageAddress(imageAddress)
                    .build();
            addImages.add(image);
        }

        imageRepository.saveAll(addImages);
        imageRepository.deleteAllById(requestDto.getDeleteImageIds()); //imageId가 존재하는지 db 조회로 검증을 해야할까 ?

    }
}
