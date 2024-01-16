package com.blocker.blocker_server.Image.service;

import com.blocker.blocker_server.Image.domain.Image;
import com.blocker.blocker_server.Image.dto.response.ImageDto;
import com.blocker.blocker_server.Image.repository.ImageRepository;
import com.blocker.blocker_server.IntegrationTestSupport;
import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.board.repository.BoardRepository;
import com.blocker.blocker_server.contract.domain.Contract;
import com.blocker.blocker_server.contract.repository.ContractRepository;
import com.blocker.blocker_server.user.domain.User;
import com.blocker.blocker_server.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ImageServiceSupportTest extends IntegrationTestSupport {

    @Autowired
    private ImageServiceSupport imageServiceSupport;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ContractRepository contractRepository;

    @AfterEach
    void tearDown() {
        imageRepository.deleteAllInBatch();
        boardRepository.deleteAllInBatch();
        contractRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("이미지 리스트를 저장한다.")
    @Test
    void saveImages() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);
        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);
        Board board = Board.create(user, "testTitle", "testContent", "testImage", "testInfo", contract);
        boardRepository.save(board);
        Image image1 = Image.create(board, "address1");
        Image image2 = Image.create(board, "address2");
        Image image3 = Image.create(board, "address3");

        /** when */

        imageServiceSupport.saveImages(List.of(image1, image2, image3));

        /** then */

        List<Image> images = imageRepository.findAll();
        assertThat(images).hasSize(3);

    }

    @DisplayName("이미지 인덱스 리스트로 이미지들을 삭제한다.")
    @Test
    void deleteImagesByIds() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);
        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);
        Board board = Board.create(user, "testTitle", "testContent", "testImage", "testInfo", contract);
        boardRepository.save(board);
        Image image1 = Image.create(board, "address1");
        Image image2 = Image.create(board, "address2");
        Image image3 = Image.create(board, "address3");
        imageRepository.saveAll(List.of(image1, image2, image3));

        /** when */

        imageServiceSupport.deleteImagesByIds(List.of(image1.getImageId(), image2.getImageId(), image3.getImageId()));

        /** then */

        List<Image> images = imageRepository.findAll();
        assertThat(images).hasSize(0);
    }

    @DisplayName("이미지 엔티티 리스트를 dto 리스트로 변환한다.")
    @Test
    void entityListToDtoList() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);
        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);
        Board board = Board.create(user, "testTitle", "testContent", "testImage", "testInfo", contract);
        boardRepository.save(board);
        Image image1 = Image.create(board, "address1");
        Image image2 = Image.create(board, "address2");
        Image image3 = Image.create(board, "address3");
        imageRepository.saveAll(List.of(image1, image2, image3));

        /** when */

        List<ImageDto> dtos = imageServiceSupport.entityListToDtoList(List.of(image1, image2, image3));

        /** then */

        List<Long> ids = dtos.stream()
                .map(dto -> dto.getImageId())
                .collect(Collectors.toList());

        assertThat(dtos).hasSize(3);
        assertThat(ids)
                .contains(image1.getImageId())
                .contains(image2.getImageId())
                .contains(image3.getImageId());

    }

    public List<ImageDto> entityListToDtoList(List<Image> images) {
        return images.stream()
                .map(image -> ImageDto.of(image))
                .collect(Collectors.toList());
    }

    @DisplayName("게시글과 이미지 주소 리스트로 이미지 엔티티 리스트를 만들어 반환한다.")
    @Test
    void createImageEntities() {

        /** given */
        User user = User.create("testEmail", "testName", "testPicture", "testValue", List.of("USER"));
        userRepository.save(user);
        Contract contract = Contract.create(user, "testTitle", "testContent");
        contractRepository.save(contract);
        Board board = Board.create(user, "testTitle", "testContent", "testImage", "testInfo", contract);
        boardRepository.save(board);

        /** when */

        List<Image> images = imageServiceSupport.createImageEntities(List.of("address1","address2","address3"), board);

        /** then */

        assertThat(images).hasSize(3);
        assertThat(images.get(0).getBoard().getBoardId()).isEqualTo(board.getBoardId());
        List<String> addresses = images.stream()
                .map(image -> image.getImageAddress())
                .collect(Collectors.toList());
        assertThat(addresses)
                .contains("address1")
                .contains("address2")
                .contains("address3");

    }


}