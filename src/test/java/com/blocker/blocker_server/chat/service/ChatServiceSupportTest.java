package com.blocker.blocker_server.chat.service;

import com.blocker.blocker_server.IntegrationTestSupport;
import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.board.repository.BoardRepository;
import com.blocker.blocker_server.board.service.BoardServiceSupport;
import com.blocker.blocker_server.chat.domain.ChatMessage;
import com.blocker.blocker_server.chat.domain.ChatRoom;
import com.blocker.blocker_server.chat.domain.ChatUser;
import com.blocker.blocker_server.chat.dto.response.GetChatMessagesResponseDto;
import com.blocker.blocker_server.chat.dto.response.GetChatRoomListDto;
import com.blocker.blocker_server.chat.exception.ChatRoomNotFoundException;
import com.blocker.blocker_server.chat.exception.IsNotChatParticipantException;
import com.blocker.blocker_server.chat.mongo.ChatMessageRepository;
import com.blocker.blocker_server.chat.repository.ChatRoomRepository;
import com.blocker.blocker_server.chat.repository.ChatUserRepository;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ChatServiceSupportTest extends IntegrationTestSupport {

    @Autowired
    private ChatServiceSupport chatServiceSupport;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ChatUserRepository chatUserRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @AfterEach
    void tearDown() {
        chatMessageRepository.deleteAll();
        chatUserRepository.deleteAllInBatch();
        chatRoomRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        contractRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("ChatMessage를 저장한다.")
    @Test
    void saveChatMessage() {

        /** given */
        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2));

        ChatRoom chatRoom = ChatRoom.create(LocalDateTime.of(2024, 1, 1, 12, 0));
        chatRoomRepository.save(chatRoom);

        ChatMessage chatMessage = ChatMessage.create(chatRoom.getChatRoomID(), user1.getName(), user1.getEmail(), "testContent", LocalDateTime.of(2024, 1, 1, 13, 0));

        /** when */

        chatServiceSupport.saveChatMessage(chatMessage);

        /** then */

        List<ChatMessage> chatMessages = chatMessageRepository.findAll();

        assertThat(chatMessages).hasSize(1);
        assertThat(chatMessages.get(0).getChatRoomId()).isEqualTo(chatRoom.getChatRoomID());
        assertThat(chatMessages.get(0).getSenderEmail()).isEqualTo(user1.getEmail());

    }

    @DisplayName("ChatRoomId로 ChatRoom을 조회한다.")
    @Test
    void getChatRoomById() {

        /** given */
        ChatRoom chatRoom = ChatRoom.create(LocalDateTime.of(2024, 1, 1, 12, 0));
        chatRoomRepository.save(chatRoom);

        /** when */

        ChatRoom result = chatServiceSupport.getChatRoomById(chatRoom.getChatRoomID());

        /** then */

        assertThat(result.getChatRoomID()).isEqualTo(chatRoom.getChatRoomID());

    }


    @DisplayName("ChatRoomId인 ChatRoom이 없으면 ChatRoomNotFoundException을 던진다.")
    @Test
    void getChatRoomByIdException() {

        /** given */

        /** when then */
        assertThatThrownBy(() -> chatServiceSupport.getChatRoomById(1l))
                .isInstanceOf(ChatRoomNotFoundException.class);

    }


    @DisplayName("채팅방을 저장한다.")
    @Test
    void saveChatRoom() {

        /** given */

        ChatRoom chatRoom = ChatRoom.create(LocalDateTime.of(2024, 1, 1, 12, 0));

        /** when */

        chatServiceSupport.saveChatRoom(chatRoom);

        /** then */

        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        assertThat(chatRooms).hasSize(1);

    }

    @DisplayName("ChatUser 엔티티 리스트를 반환한다.")
    @Test
    void createChatUsers() {

        /** given */
        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2, user3));

        ChatRoom chatRoom = ChatRoom.create(LocalDateTime.of(2024, 1, 1, 12, 0));
        chatRoomRepository.save(chatRoom);


        /** when */

        List<ChatUser> chatUsers = chatServiceSupport.createChatUsers(chatRoom, user1, List.of("testEmail2", "testEmail3"));

        /** then */

        assertThat(chatUsers).hasSize(3);
        List<String> chatUserEmails = chatUsers.stream()
                .map(chatUser -> chatUser.getUser().getEmail())
                .collect(Collectors.toList());

        assertThat(chatUserEmails).contains("testEmail1");
        assertThat(chatUserEmails).contains("testEmail2");
        assertThat(chatUserEmails).contains("testEmail3");

    }

    @DisplayName("ChatUser들을 저장한다.")
    @Test
    void saveChatUsers() {

        /** given */
        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2, user3));

        ChatRoom chatRoom = ChatRoom.create(LocalDateTime.of(2024, 1, 1, 12, 0));
        chatRoomRepository.save(chatRoom);

        ChatUser chatUser1 = ChatUser.create(user1, chatRoom);
        ChatUser chatUser2 = ChatUser.create(user2, chatRoom);
        ChatUser chatUser3 = ChatUser.create(user3, chatRoom);

        /** when */

        chatServiceSupport.saveChatUsers(List.of(chatUser1, chatUser2, chatUser3));

        /** then */

        List<ChatUser> chatUsers = chatUserRepository.findAll();
        assertThat(chatUsers).hasSize(3);

    }

    @DisplayName("나의 채팅방 리스트를 조회한다.")
    @Test
    void getChatRoomsByUser() {

        /** given */
        User me = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User user3 = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(me, user2, user3));

        ChatRoom chatRoom = ChatRoom.create(LocalDateTime.of(2024, 1, 1, 12, 0));
        ChatRoom chatRoom2 = ChatRoom.create(LocalDateTime.of(2024, 1, 1, 12, 0));
        ChatRoom chatRoom3 = ChatRoom.create(LocalDateTime.of(2024, 1, 1, 12, 0));

        chatRoomRepository.saveAll(List.of(chatRoom, chatRoom2, chatRoom3));

        ChatUser chatUser1 = ChatUser.create(me, chatRoom);
        ChatUser chatUser2 = ChatUser.create(user2, chatRoom);
        ChatUser chatUser3 = ChatUser.create(user3, chatRoom);

        ChatUser chatUser4 = ChatUser.create(me, chatRoom2);
        ChatUser chatUser5 = ChatUser.create(user2, chatRoom2);
        ChatUser chatUser6 = ChatUser.create(user3, chatRoom2);


        ChatUser chatUser7 = ChatUser.create(user2, chatRoom3);
        ChatUser chatUser8 = ChatUser.create(user3, chatRoom3);

        chatUserRepository.saveAll(List.of(chatUser1, chatUser2, chatUser3, chatUser4, chatUser5, chatUser6, chatUser7, chatUser8));

        /** when */

        List<ChatRoom> chatRooms = chatServiceSupport.getChatRoomsByUser(me);

        /** then */

        assertThat(chatRooms).hasSize(2);
    }

    @DisplayName("채팅방 리스트를 dto로 변환한다.")
    @Test
    void createChatRoomListResponseDto() {

        /** given */

        ChatRoom chatRoom = ChatRoom.create(LocalDateTime.of(2024, 1, 1, 12, 0));
        ChatRoom chatRoom2 = ChatRoom.create(LocalDateTime.of(2024, 1, 1, 12, 0));
        ChatRoom chatRoom3 = ChatRoom.create(LocalDateTime.of(2024, 1, 1, 12, 0));

        chatRoomRepository.saveAll(List.of(chatRoom, chatRoom2, chatRoom3));

        /** when */

        List<GetChatRoomListDto> result = chatServiceSupport.createChatRoomListResponseDto(List.of(chatRoom, chatRoom2, chatRoom3));

        /** then */

        assertThat(result).hasSize(3);
        List<Long> ids = result.stream()
                .map(o -> o.getChatRoomId())
                .collect(Collectors.toList());
        assertThat(ids)
                .contains(chatRoom.getChatRoomID())
                .contains(chatRoom2.getChatRoomID())
                .contains(chatRoom3.getChatRoomID());

    }

    @DisplayName("채팅방 참여자가 아니면 IsNotChatParticipantException을 던진다.")
    @Test
    void checkIsChatParticipant() {

        /** given */

        User participant1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User participant2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        User notParticipant = User.create("testEmail3", "testName3", "testPicture", "testValue3", List.of("USER"));
        userRepository.saveAll(List.of(participant1, participant2, notParticipant));

        ChatRoom chatRoom = ChatRoom.create(LocalDateTime.of(2024, 1, 1, 12, 0));
        chatRoomRepository.save(chatRoom);

        ChatUser chatUser1 = ChatUser.create(participant1, chatRoom);
        ChatUser chatUser2 = ChatUser.create(participant2, chatRoom);
        chatUserRepository.saveAll(List.of(chatUser1, chatUser2));


        /** when then */

        assertThatThrownBy(() -> chatServiceSupport.checkIsChatParticipant(notParticipant, chatRoom.getChatRoomID()))
                .isInstanceOf(IsNotChatParticipantException.class);
    }

    @DisplayName("채팅방 인덱스로 해당 채팅방의 메세지들을 조회한다.")
    @Test
    void getChatMessagesByChatRoomId() {

        /** given */

        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2));

        ChatRoom chatRoom = ChatRoom.create(LocalDateTime.of(2024, 1, 1, 12, 0));
        chatRoomRepository.save(chatRoom);

        ChatUser chatUser1 = ChatUser.create(user1, chatRoom);
        ChatUser chatUser2 = ChatUser.create(user2, chatRoom);
        chatUserRepository.saveAll(List.of(chatUser1, chatUser2));

        ChatMessage chatMessage1 = ChatMessage.create(chatRoom.getChatRoomID(), user1.getName(), user1.getEmail(), "content1", LocalDateTime.now());
        ChatMessage chatMessage2 = ChatMessage.create(chatRoom.getChatRoomID(), user2.getName(), user2.getEmail(), "content2", LocalDateTime.now());
        ChatMessage chatMessage3 = ChatMessage.create(chatRoom.getChatRoomID(), user1.getName(), user1.getEmail(), "content3", LocalDateTime.now());
        ChatMessage chatMessage4 = ChatMessage.create(chatRoom.getChatRoomID(), user2.getName(), user2.getEmail(), "content4", LocalDateTime.now());
        ChatMessage chatMessage5 = ChatMessage.create(chatRoom.getChatRoomID(), user1.getName(), user1.getEmail(), "content5", LocalDateTime.now());
        chatMessageRepository.saveAll(List.of(chatMessage1, chatMessage2, chatMessage3, chatMessage4, chatMessage5));

        /** when */
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomId(chatRoom.getChatRoomID(), PageRequest.of(0, 10));

        /** then */
        assertThat(messages).hasSize(5);

    }

    @DisplayName("ChatMessage 리스트를 dto 리스트로 반환한다.")
    @Test
    void createChatMessageResponseDto() {

        /** given */

        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User user2 = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2));

        ChatRoom chatRoom = ChatRoom.create(LocalDateTime.of(2024, 1, 1, 12, 0));
        chatRoomRepository.save(chatRoom);

        ChatUser chatUser1 = ChatUser.create(user1, chatRoom);
        ChatUser chatUser2 = ChatUser.create(user2, chatRoom);
        chatUserRepository.saveAll(List.of(chatUser1, chatUser2));

        ChatMessage chatMessage1 = ChatMessage.create(chatRoom.getChatRoomID(), user1.getName(), user1.getEmail(), "content1", LocalDateTime.now());
        ChatMessage chatMessage2 = ChatMessage.create(chatRoom.getChatRoomID(), user2.getName(), user2.getEmail(), "content2", LocalDateTime.now());
        ChatMessage chatMessage3 = ChatMessage.create(chatRoom.getChatRoomID(), user1.getName(), user1.getEmail(), "content3", LocalDateTime.now());
        ChatMessage chatMessage4 = ChatMessage.create(chatRoom.getChatRoomID(), user2.getName(), user2.getEmail(), "content4", LocalDateTime.now());
        ChatMessage chatMessage5 = ChatMessage.create(chatRoom.getChatRoomID(), user1.getName(), user1.getEmail(), "content5", LocalDateTime.now());
        chatMessageRepository.saveAll(List.of(chatMessage1, chatMessage2, chatMessage3, chatMessage4, chatMessage5));

        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(chatRoom.getChatRoomID(), PageRequest.of(0, 10));

        /** when */

        List<GetChatMessagesResponseDto> result = chatServiceSupport.createChatMessageResponseDto(chatMessages);

        /** then */

        assertThat(result).hasSize(5);

    }

    @DisplayName("게시글 작성자를 반환한다.")
    @Test
    void getBoardWriter() {

        /** given */
        User user1 = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        userRepository.save(user1);

        Contract contract = Contract.create(user1, "testTitle", "testContent");
        contractRepository.save(contract);

        Board board = Board.create(user1, "testTitle", "testContent", "testImage", "testInfo", contract);
        boardRepository.save(board);

        /** when */

        User boardWriter = chatServiceSupport.getBoardWriter(board.getBoardId());

        /** then */
        assertThat(boardWriter.getEmail()).isEqualTo(user1.getEmail());

    }

    @DisplayName("게시글 작성자와 일대일 채팅방이 있을 경우 해당 채팅방의 인덱스를 반환한다.")
    @Test
    void getOneToOneChatRoomByUsers() {

        /** given */
        User me = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User boardWriter = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(me, boardWriter));

        ChatRoom chatRoom = ChatRoom.create(LocalDateTime.of(2024, 1, 1, 12, 0));
        chatRoomRepository.save(chatRoom);

        ChatUser chatUser1 = ChatUser.create(me, chatRoom);
        ChatUser chatUser2 = ChatUser.create(boardWriter, chatRoom);
        chatUserRepository.saveAll(List.of(chatUser1, chatUser2));

        /** when */

        Long result = chatServiceSupport.getOneToOneChatRoomByUsers(me.getEmail(), boardWriter.getEmail());

        /** then */

        assertThat(result).isEqualTo(chatRoom.getChatRoomID());
    }

    @DisplayName("게시글 작성자와 일대일 채팅방이 없을 경우 채팅방을 만들고 해당 채팅방의 인덱스를 반환한다.")
    @Test
    void getOneToOneChatRoomByUsersNotExist() {

        /** given */
        User me = User.create("testEmail1", "testName1", "testPicture", "testValue1", List.of("USER"));
        User boardWriter = User.create("testEmail2", "testName2", "testPicture", "testValue2", List.of("USER"));
        userRepository.saveAll(List.of(me, boardWriter));

        /** when */

        Long result = chatServiceSupport.getOneToOneChatRoomByUsers(me.getEmail(), boardWriter.getEmail());

        /** then */

        assertThat(result).isNotNull();

    }
}