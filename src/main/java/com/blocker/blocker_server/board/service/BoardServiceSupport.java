package com.blocker.blocker_server.board.service;

import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.board.dto.response.GetBoardListResponseDto;
import com.blocker.blocker_server.board.exception.BoardNotFoundException;
import com.blocker.blocker_server.board.exception.UnauthorizedDeleteBoardException;
import com.blocker.blocker_server.board.exception.UnauthorizedModifyBoardException;
import com.blocker.blocker_server.board.repository.BoardRepository;
import com.blocker.blocker_server.bookmark.repository.BookmarkRepository;
import com.blocker.blocker_server.commons.exception.ForbiddenException;
import com.blocker.blocker_server.commons.exception.NotFoundException;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.service.ContractServiceSupport;
import com.blocker.blocker_server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceSupport {

    private final BoardRepository boardRepository;
    private final ContractServiceSupport contractServiceSupport;
    private final BookmarkRepository bookmarkRepository;

    public Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("board id: " + boardId));
    }

    public List<Board> getBoardList(Pageable pageable) {
        return boardRepository.getBoardList(pageable);
    }

    public Board getBoardWithImages(Long boardId) {
        return boardRepository.getBoardWithImages(boardId).orElseThrow(()-> new BoardNotFoundException("board id: " + boardId));
    }

    @Transactional
    public void save(Board board) {
        boardRepository.save(board);
    }

    public List<GetBoardListResponseDto> createBoardListResponseDto(List<Board> entityList) {

        return entityList.stream()
                .map(board -> GetBoardListResponseDto.of(board))
                .collect(Collectors.toList());
    }

    public boolean checkIsWriter(String me, String boardUser) {
        return me.equals(boardUser);
    }

    public boolean checkIsBookmarked(User me, Board board) {
        //return bookmarkProvider.isBookmark(me, boardId);
        return bookmarkRepository.existsByUserAndBoard(me, board);
    }

    public void checkDeleteAuthority(String me, String boardWriter) {
        if(!me.equals(boardWriter))
            throw new UnauthorizedDeleteBoardException("user: " + me);
    }

    public void checkModifyAuthority(String me, String boardWriter) {
        if(!me.equals(boardWriter))
            throw new UnauthorizedModifyBoardException("user: " + me);
    }

    @Transactional
    public void deleteBoardById(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    @Transactional
    public Contract modifyContractBelongingToBoard(String me, Board board, Long modifyContractId) {

        //계약서가 달라졌으면 조회 후 나의 계약서가 맞는지 확인
        if (!board.getContract().getContractId().equals(modifyContractId)) {
            Contract contract = contractServiceSupport.getContractById(modifyContractId);

            contractServiceSupport.checkIsContractWriter(me, contract);

            return contract;
        }

        return contractServiceSupport.getContractById(modifyContractId);
    }

    public List<Board> getMyBoards(String me, Pageable pageable) {
        return boardRepository.getMyBoards(me, pageable);
    }
}
