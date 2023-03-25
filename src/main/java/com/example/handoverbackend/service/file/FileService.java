package com.example.handoverbackend.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void upload(MultipartFile file, String filename);
    void delete(String filename);
}
