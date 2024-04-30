package com.blocker.blocker_server.board.service;

import com.blocker.blocker_server.Image.service.ImageServiceSupport;
import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.Image.domain.Image;
import com.blocker.blocker_server.board.dto.request.ModifyBoardRequestDto;
import com.blocker.blocker_server.board.dto.request.SaveBoardRequestDto;
import com.blocker.blocker_server.board.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.board.dto.response.GetBoardListResponseDtoInterface;
import com.blocker.blocker_server.board.dto.response.GetBoardResponseDto;
import com.blocker.blocker_server.Image.dto.response.ImageDto;
import com.blocker.blocker_server.commons.utils.CurrentUserGetter;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.service.ContractServiceSupport;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final ImageServiceSupport imageServiceSupport;
    private final BoardServiceSupport boardServiceSupport;
    private final ContractServiceSupport contractServiceSupport;
    private final CurrentUserGetter currentUserGetter;

    public List<GetBoardListResponseDto> getBoards(Pageable pageable) {

        List<Board> entityList = boardServiceSupport.getBoardList(pageable);

        return boardServiceSupport.createBoardListResponseDto(entityList);
    }


    public GetBoardResponseDto getBoard(Long boardId) {

        User me = currentUserGetter.getCurrentUser();

        //image fetch join한 게시글
        Board board = boardServiceSupport.getBoardWithImages(boardId);

        //내가 작성자인지
        boolean isWriter = boardServiceSupport.checkIsWriter(me.getEmail(), board.getUser().getEmail());

        //내가 북마크한 게시글인지
        boolean isBookmark = boardServiceSupport.checkIsBookmarked(me, board);

        //이미지 주소
        List<ImageDto> imageAddresses = imageServiceSupport.entityListToDtoList(board.getImages());

        return GetBoardResponseDto.of(board, imageAddresses, isWriter, isBookmark);
    }




    @Transactional
    public void saveBoard(SaveBoardRequestDto requestDto) {

        User me = currentUserGetter.getCurrentUser();

        //게시글에 포함시킬 계약서
        Contract contract = contractServiceSupport.getContractById(requestDto.getContractId());

        //자신의 계약서가 맞는지 검사
        contractServiceSupport.checkIsContractWriter(me.getEmail(), contract);

        //저장할 게시글
        Board newBoard = Board.create(
                me,
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getInfo(),
                requestDto.getRepresentImage(),
                contract);

        //게시글의 이미지들
        List<Image> images = imageServiceSupport.createImageEntities(requestDto.getImages(), newBoard);

        //게시글 저장
        boardServiceSupport.save(newBoard);
        //이미지 저장
        imageServiceSupport.saveImages(images);

    }

    @Transactional
    public void deleteBoard(Long boardId) {

        User me = currentUserGetter.getCurrentUser();

        //삭제할 게시글
        Board board = boardServiceSupport.getBoardById(boardId);

        //게시글 작성자가 맞는지 검사
        boardServiceSupport.checkDeleteAuthority(me.getEmail(), board.getUser().getEmail());

        //게시글 삭제
        boardServiceSupport.deleteBoardById(boardId);
    }

    @Transactional
    public void modifyBoard(Long boardId, ModifyBoardRequestDto requestDto) {

        User me = currentUserGetter.getCurrentUser();

        //수정할 게시글
        Board board = boardServiceSupport.getBoardById(boardId);

        //게시글 작성자가 맞는지 검사
        boardServiceSupport.checkModifyAuthority(me.getEmail(), board.getUser().getEmail());

        //게시글에 포함된 계약서 수정
        Contract contract = boardServiceSupport.modifyContractBelongingToBoard(me.getEmail(), board, requestDto.getContractId());

        //Dynamic insert로 바뀐 값만 업데이트 됨.
        board.updateBoard(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getInfo(),
                requestDto.getRepresentImage(),
                contract);

        //추가된 이미지 저장
        List<Image> addImages = imageServiceSupport.createImageEntities(requestDto.getAddImageAddresses(), board);
        imageServiceSupport.saveImages(addImages);

        //없앨 이미지 삭제
        imageServiceSupport.deleteImagesByIds(requestDto.getDeleteImageIds()); //imageId가 존재하는지 db 조회로 검증을 해야할까 ?

    }


    public List<GetBoardListResponseDto> getMyBoards(Pageable pageable) {

        User me = currentUserGetter.getCurrentUser();

        List<Board> entityList = boardServiceSupport.getMyBoards(me.getEmail(), pageable);

        return boardServiceSupport.createBoardListResponseDto(entityList);
    }


    public List<GetBoardListResponseDtoInterface> getBoardsByNativeQuery(Pageable pageable) {
        return boardServiceSupport.getBoardsByNativeQuery(pageable);
    }
}


