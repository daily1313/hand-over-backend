package com.example.handoverbackend.factory;

import com.example.handoverbackend.domain.board.Image;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class ImageMaker {

    public static List<MultipartFile> createImages(){
        List<MultipartFile> images = new ArrayList<>();
        images.add(new MockMultipartFile("test1","test1.png", MediaType.IMAGE_PNG_VALUE,"test1".getBytes()));
        images.add(new MockMultipartFile("test2","test2.png", MediaType.IMAGE_PNG_VALUE,"test2".getBytes()));
        return images;
    }

    public static Image createImage(){
        return new Image("image.jpg");
    }
}
