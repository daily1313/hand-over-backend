package com.example.handoverbackend.domain.board;

import com.example.handoverbackend.domain.common.BaseEntity;
import com.example.handoverbackend.exception.UnsupportedImageFormatException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(nullable = false)
    private String uniqueName;

    @Column(nullable = false)
    private String originName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    private final static List<String> supportedExtension = List.of("jpg", "jpeg", "gif", "bmp", "png");

    public Image(String originName) {
        this.originName = originName;
        this.uniqueName = generateUniqueName(extractExtension(originName));
    }

    public void initBoard(Board board) {
        if (this.board == null) {
            this.board = board;
        }
    }

    private String generateUniqueName(String extension) {
        return UUID.randomUUID() + "." + extension;
    }

    private String extractExtension(String originName) {
        String extension = originName.substring(originName.lastIndexOf(".") + 1);
        if (isSupportedFormat(extension)) {
            return extension;
        }
        throw new UnsupportedImageFormatException();
    }

    private boolean isSupportedFormat(String extension) {
        return supportedExtension.contains(extension);
    }
}
