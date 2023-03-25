package com.example.handoverbackend.domain.board;

import com.example.handoverbackend.domain.category.Category;
import com.example.handoverbackend.domain.common.BaseEntity;
import com.example.handoverbackend.domain.member.Member;
import com.example.handoverbackend.dto.board.BoardUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Category category;

    @OneToMany(mappedBy = "board",fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Image> images;

    public static Board createBoard(String title, String content, Member member,Category category, List<Image> images) {
        return new Board(title, content, member, category, images);
    }

    private Board(String title, String content, Member member,Category category, List<Image> images) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.category = category;
        this.images = new ArrayList<>();
        addImages(images);
    }

    public ImageUpdatedResult update(BoardUpdateRequestDto req) {
        this.title = req.getTitle();
        this.content = req.getContent();
        ImageUpdatedResult result = findImageUpdatedResult(req.getAddedImages(), req.getDeletedImages());
        addImages(result.getAddedImages());
        deleteImages(result.getDeletedImages());
        onPreUpdate();
        return result;
    }

    private void addImages(List<Image> added) {
        added.forEach(image -> {
            this.images.add(image);
            image.initBoard(this);
        });
    }

    private void deleteImages(List<Image> deletedImages) {
        deletedImages.forEach(deleteImageId -> this.images.remove(deleteImageId));
    }

    private ImageUpdatedResult findImageUpdatedResult(List<MultipartFile> addedImageFiles, List<Long> deletedImageIds) {
        List<Image> addedImages = convertImageFilesToImages(addedImageFiles);
        List<Image> deletedImages = convertImageIdsToImages(deletedImageIds);
        return new ImageUpdatedResult(addedImageFiles, addedImages, deletedImages);
    }

    private List<Image> convertImageIdsToImages(List<Long> imageIds) {
        return imageIds.stream()
            .map(this::convertImageIdToImage)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toList());
    }

    private Optional<Image> convertImageIdToImage(Long id) {
        return this.images.stream()
            .filter(image -> Objects.equals(image.getId(), id))
            .findAny();
    }

    private List<Image> convertImageFilesToImages(List<MultipartFile> imageFiles) {
        return imageFiles.stream()
            .map(imageFile -> new Image(imageFile.getOriginalFilename()))
            .collect(toList());
    }

    public boolean isOwnBoard(Member member) {
        return this.member.equals(member);
    }

    @Getter
    @AllArgsConstructor
    public static class ImageUpdatedResult {
        private List<MultipartFile> addedImageFiles;
        private List<Image> addedImages;
        private List<Image> deletedImages;
    }
}
