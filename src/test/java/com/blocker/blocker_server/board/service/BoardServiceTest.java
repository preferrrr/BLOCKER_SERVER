package com.blocker.blocker_server.board.service;

import com.blocker.blocker_server.Image.service.ImageServiceSupport;
import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.Image.domain.Image;
import com.blocker.blocker_server.board.dto.request.ModifyBoardRequestDto;
import com.blocker.blocker_server.board.dto.request.SaveBoardRequestDto;
import com.blocker.blocker_server.board.dto.response.GetBoardResponseDto;
import com.blocker.blocker_server.commons.utils.CurrentUserGetter;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.service.ContractServiceSupport;
import com.blocker.blocker_server.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;
    @Mock
    private ImageServiceSupport imageServiceSupport;
    @Mock
    private BoardServiceSupport boardServiceSupport;
    @Mock
    private ContractServiceSupport contractServiceSupport;
    @Mock
    private CurrentUserGetter currentUserGetter;


    private Board board1;
    private Board board2;
    private Board board3;
    private User user;

    private Contract contract;
    private Image image1;
    private Image image2;
    private Image image3;


    @BeforeEach
    void setUp() {
        user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        contract = Contract.create(user, "testTitle", "testContent");
        board1 = Board.create(user, "testTitle1", "testContent1", "testImage", "testInfo", contract);
        board2 = Board.create(user, "testTitle2", "testContent2", "testImage", "testInfo", contract);
        board3 = Board.create(user, "testTitle3", "testContent3", "testImage", "testInfo", contract);

        image1 = Image.create(board1, "testAddress");
        image2 = Image.create(board1, "testAddress");
        image3 = Image.create(board1, "testAddress");
    }

    @DisplayName("게시글 리스트를 조회한다.")
    @Test
    void getBoards() {

        /** given */

        given(boardServiceSupport.getBoardList(any(Pageable.class))).willReturn(List.of(board1, board2, board3));
        given(boardServiceSupport.createBoardListResponseDto(any(List.class))).willReturn(mock(List.class));
        Pageable pageable = PageRequest.of(0, 10);

        /** when */

        boardService.getBoards(pageable);

        /** then */

        verify(boardServiceSupport, times(1)).getBoardList(any(Pageable.class));
        verify(boardServiceSupport, times(1)).createBoardListResponseDto(any(List.class));

    }

    @DisplayName("게시글 인덱스로 게시글을 조회한다.")
    @Test
    void getBoard() {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(boardServiceSupport.getBoardWithImages(any(Long.class))).willReturn(board1);
        given(boardServiceSupport.checkIsWriter(anyString(), anyString())).willReturn(true);
        given(boardServiceSupport.checkIsBookmarked(any(User.class), any(Board.class))).willReturn(true);
        given(imageServiceSupport.entityListToDtoList(any())).willReturn(mock(List.class));

        /** when */

        GetBoardResponseDto response = boardService.getBoard(1l);

        /** then */

        verify(boardServiceSupport, times(1)).getBoardWithImages(any(Long.class));
        verify(boardServiceSupport, times(1)).checkIsWriter(anyString(), anyString());
        verify(boardServiceSupport, times(1)).checkIsBookmarked(any(User.class), any(Board.class));
        verify(imageServiceSupport, times(1)).entityListToDtoList(any(List.class));
        assertThat(response.getTitle()).isEqualTo("testTitle1");
        assertThat(response.getName()).isEqualTo(user.getName());
        assertThat(response.getIsWriter()).isEqualTo(true);
        assertThat(response.getIsBookmark()).isEqualTo(true);
    }

    @DisplayName("게시글을 저장한다.")
    @Test
    void saveBoard() {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(contractServiceSupport.getContractById(anyLong())).willReturn(mock(Contract.class));
        willDoNothing().given(contractServiceSupport).checkIsContractWriter(anyString(), any(Contract.class));
        given(imageServiceSupport.createImageEntities(any(List.class), any(Board.class))).willReturn(List.of(image1, image2, image3));

        SaveBoardRequestDto dto = SaveBoardRequestDto.builder()
                .title("testTitle")
                .content("testContent")
                .contractId(1l)
                .info("testInfo")
                .images(List.of("testImage1", "testImage2"))
                .representImage("testImage")
                .build();

        /** when */

        boardService.saveBoard(dto);

        /** then */

        verify(contractServiceSupport, times(1)).getContractById(anyLong());
        verify(contractServiceSupport, times(1)).checkIsContractWriter(anyString(), any(Contract.class));
        verify(imageServiceSupport, times(1)).createImageEntities(any(List.class), any(Board.class));
        verify(boardServiceSupport, times(1)).save(any(Board.class));
        verify(imageServiceSupport, times(1)).saveImages(any(List.class));

    }

    @DisplayName("게시글을 삭제한다.")
    @Test
    void deleteBoard() {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(boardServiceSupport.getBoardById(anyLong())).willReturn(board1);
        willDoNothing().given(boardServiceSupport).checkDeleteAuthority(anyString(), anyString());

        /** when */

        boardService.deleteBoard(1l);

        /** then */

        verify(boardServiceSupport, times(1)).getBoardById(anyLong());
        verify(boardServiceSupport, times(1)).checkDeleteAuthority(anyString(), anyString());
        verify(boardServiceSupport, times(1)).deleteBoardById(anyLong());

    }


    @DisplayName("게시글을 수정한다.")
    @Test
    void modifyBoard() {

        /** given */

        ModifyBoardRequestDto request = ModifyBoardRequestDto.builder()
                .title("testTitle")
                .content("testContent")
                .info("testInfo")
                .representImage("testImage")
                .addImageAddresses(List.of("testAddress1", "testAddress2"))
                .deleteImageIds(List.of(1l, 2l, 3l))
                .contractId(1l)
                .build();

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(boardServiceSupport.getBoardById(anyLong())).willReturn(board1);
        willDoNothing().given(boardServiceSupport).checkModifyAuthority(anyString(), anyString());
        given(boardServiceSupport.modifyContractBelongingToBoard(anyString(), any(Board.class), anyLong())).willReturn(contract);
        given(imageServiceSupport.createImageEntities(anyList(), any(Board.class))).willReturn(List.of(image1, image2, image3));


        /** when */

        boardService.modifyBoard(1l, request);

        /** then */

        verify(boardServiceSupport, times(1)).getBoardById(anyLong());
        verify(boardServiceSupport, times(1)).checkModifyAuthority(anyString(), anyString());
        verify(imageServiceSupport, times(1)).createImageEntities(anyList(), any(Board.class));
        verify(imageServiceSupport, times(1)).saveImages(anyList());
        verify(imageServiceSupport, times(1)).deleteImagesByIds(anyList());

    }

    @DisplayName("내가 쓴 게시글을 조회한다.")
    @Test
    void getMyBoards() {

        /** given */

        given(currentUserGetter.getCurrentUser()).willReturn(user);
        given(boardServiceSupport.getMyBoards(anyString(), any(Pageable.class))).willReturn(List.of(board1, board2, board3));
        given(boardServiceSupport.createBoardListResponseDto(anyList())).willReturn(mock(List.class));

        /** when */

        boardService.getMyBoards(PageRequest.of(0, 10));

        /** then */

        verify(boardServiceSupport, times(1)).getMyBoards(anyString(),any(Pageable.class));
        verify(boardServiceSupport, times(1)).createBoardListResponseDto(anyList());

    }


}