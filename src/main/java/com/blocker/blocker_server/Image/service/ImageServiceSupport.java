package com.blocker.blocker_server.Image.service;

import com.blocker.blocker_server.board.domain.Board;
import com.blocker.blocker_server.Image.domain.Image;
import com.blocker.blocker_server.Image.dto.response.ImageDto;
import com.blocker.blocker_server.Image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageServiceSupport {

    private final ImageRepository imageRepository;

    @Transactional
    public void saveImages(List<Image> images) {
        imageRepository.saveAll(images);
    }

    @Transactional
    public void deleteImagesByIds(List<Long> imageIds) {
        imageRepository.deleteAllById(imageIds);
    }

    public List<ImageDto> entityListToDtoList(List<Image> images) {
        return images.stream()
                .map(image -> ImageDto.of(image))
                .collect(Collectors.toList());
    }

    public List<Image> createImageEntities(List<String> imageAddresses, Board board) {
        return imageAddresses.stream()
                .map(imageAddress -> Image.create(board, imageAddress))
                .collect(Collectors.toList());

    }

}
