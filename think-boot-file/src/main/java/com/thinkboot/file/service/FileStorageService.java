package com.thinkboot.file.service;

import com.thinkboot.file.model.FileUploadResult;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    FileUploadResult upload(MultipartFile file);

    FileUploadResult upload(MultipartFile file, String directory);

    boolean delete(String fileUrl);
}