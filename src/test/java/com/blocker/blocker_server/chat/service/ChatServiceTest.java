package com.blocker.blocker_server.chat.service;

import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.board.repository.BoardRepository;
import com.blocker.blocker_server.chat.domain.ChatRoom;
import com.blocker.blocker_server.chat.domain.ChatUser;
import com.blocker.blocker_server.chat.dto.response.GetOneToOneChatRoomResponse;
import com.blocker.blocker_server.chat.repository.ChatRoomRepository;
import com.blocker.blocker_server.chat.repository.ChatUserRepository;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatUserRepository chatUserRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ContractRepository contractRepository;

    @DisplayName("게시글 작성자와 1:1 채팅방이 있으면, 그 채팅방의 인덱스를 반환한다.")
    @Test
    void getOneToOneChatRoomIdExist() {

        /** given */

        User user1  = User.create("testEmail1","testName1","testPicture1","testValue1", List.of("USER"));
        User user2  = User.create("testEmail2","testName2","testPicture2","testValue2", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2));

        Contract contract = Contract.create(user1,"testTitle","testContent");
        contractRepository.save(contract);

        Board board = Board.create(user1,"testTitle","testContent","testImage","testInfo",contract);
        boardRepository.save(board);

        ChatRoom chatRoom = ChatRoom.create(LocalDateTime.now());
        chatRoomRepository.save(chatRoom);

        ChatUser chatUser1 = ChatUser.create(user1, chatRoom);
        ChatUser chatUser2 = ChatUser.create(user2, chatRoom);
        chatUserRepository.saveAll(List.of(chatUser1, chatUser2));

        /** when */

        GetOneToOneChatRoomResponse response = chatService.getOneToOneChatRoomId(user2, board.getBoardId());

        /** then */

        assertThat(response).isNotNull();

    }

    @DisplayName("게시글 작성자와 1:1 채팅방이 없으면, 채팅방을 만들고 그 채팅방의 인덱스를 반환한다.")
    @Test
    void getOneToOneChatRoomIdNoExist() {

        /** given */

        User user1  = User.create("testEmail1","testName1","testPicture1","testValue1", List.of("USER"));
        User user2  = User.create("testEmail2","testName2","testPicture2","testValue2", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2));

        Contract contract = Contract.create(user1,"testTitle","testContent");
        contractRepository.save(contract);

        Board board = Board.create(user1,"testTitle","testContent","testImage","testInfo",contract);
        boardRepository.save(board);

        /** when */

        GetOneToOneChatRoomResponse response = chatService.getOneToOneChatRoomId(user2, board.getBoardId());

        /** then */

        assertThat(response).isNotNull();

    }

}