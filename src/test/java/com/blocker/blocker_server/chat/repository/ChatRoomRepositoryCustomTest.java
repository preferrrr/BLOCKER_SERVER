package com.blocker.blocker_server.chat.repository;

import com.blocker.blocker_server.IntegrationTestSupport;
import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.board.repository.BoardRepository;
import com.blocker.blocker_server.chat.domain.ChatRoom;
import com.blocker.blocker_server.chat.domain.ChatUser;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.repository.UserRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
class ChatRoomRepositoryCustomTest extends IntegrationTestSupport {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatUserRepository chatUserRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ContractRepository contractRepository;

    @DisplayName("1:1 채팅방의 인덱스를 조회한다.")
    @Test
    void findOneToOneChatRoomByUsersExist() {

        /** given */

        User user1  = User.create("testEmail1","testName1","testPicture1","testValue1", List.of("USER"));
        User user2  = User.create("testEmail2","testName2","testPicture2","testValue2", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2));

        ChatRoom chatRoom = ChatRoom.create(LocalDateTime.now());
        chatRoomRepository.save(chatRoom);

        ChatUser chatUser1 = ChatUser.create(user1, chatRoom);
        ChatUser chatUser2 = ChatUser.create(user2, chatRoom);
        chatUserRepository.saveAll(List.of(chatUser1, chatUser2));

        /** when */
        Long chatRoomId = chatRoomRepository.findOneToOneChatRoomByUsers(user1.getEmail(), user2.getEmail());


        /** then */
        assertThat(chatRoomId).isNotNull();

        List<ChatUser> chatUsers = chatUserRepository.findByChatRoom(chatRoomRepository.getReferenceById(chatRoomId));

        assertThat(chatUsers).hasSize(2);

    }


    @DisplayName("1:1 채팅방이 없으면 null을 반환한다.")
    @Test
    void findOneToOneChatRoomByUsersNoExist() {

        /** given */

        User user1  = User.create("testEmail1","testName1","testPicture1","testValue1", List.of("USER"));
        User user2  = User.create("testEmail2","testName2","testPicture2","testValue2", List.of("USER"));
        userRepository.saveAll(List.of(user1, user2));

        /** when */
        Long chatRoomId = chatRoomRepository.findOneToOneChatRoomByUsers(user1.getEmail(), user2.getEmail());

        /** then */
        assertThat(chatRoomId).isNull();

    }

}