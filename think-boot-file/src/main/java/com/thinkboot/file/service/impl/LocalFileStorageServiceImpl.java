package com.thinkboot.file.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.thinkboot.common.exception.BusinessException;
import com.thinkboot.file.config.FileProperties;
import com.thinkboot.file.model.FileUploadResult;
import com.thinkboot.file.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@ConditionalOnProperty(prefix = "thinkboot.file", name = "storage-type", havingValue = "local", matchIfMissing = true)
public class LocalFileStorageServiceImpl implements FileStorageService {

    private final FileProperties fileProperties;

    public LocalFileStorageServiceImpl(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }

    @PostConstruct
    public void init() {
        File storageDir = new File(fileProperties.getLocalPath());
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
    }

    @Override
    public FileUploadResult upload(MultipartFile file) {
        return upload(file, null);
    }

    @Override
    public FileUploadResult upload(MultipartFile file, String directory) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }

        long maxSize = fileProperties.getMaxFileSize() * 1024L * 1024L;
        if (file.getSize() > maxSize) {
            throw new BusinessException("文件大小不能超过 " + fileProperties.getMaxFileSize() + "MB");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = FileUtil.extName(originalFilename);
        List<String> allowedExtensions = Arrays.asList(fileProperties.getAllowedExtensions().split(","));
        if (!allowedExtensions.contains(extension.toLowerCase())) {
            throw new BusinessException("不支持的文件类型: " + extension);
        }

        String datePath = DateUtil.format(DateUtil.date(), "yyyyMMdd");
        String targetDir = fileProperties.getLocalPath();
        if (StrUtil.isNotBlank(directory)) {
            targetDir += File.separator + directory;
        }
        targetDir += File.separator + datePath;

        File dirFile = new File(targetDir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        String newFileName = IdUtil.fastSimpleUUID() + "." + extension;
        File targetFile = new File(targetDir, newFileName);

        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }

        String fileUrl = fileProperties.getBaseUrl() + "/" + 
                (StrUtil.isNotBlank(directory) ? directory + "/" : "") + 
                datePath + "/" + newFileName;

        FileUploadResult result = new FileUploadResult();
        result.setUrl(fileUrl);
        result.setName(originalFilename);
        result.setSize(file.getSize());
        result.setType(extension);
        
        return result;
    }

    @Override
    public boolean delete(String fileUrl) {
        String relativePath = fileUrl.replace(fileProperties.getBaseUrl(), "");
        String fullPath = fileProperties.getLocalPath() + relativePath.replace("/", File.separator);
        
        File file = new File(fullPath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}