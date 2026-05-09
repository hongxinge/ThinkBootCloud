package com.thinkboot.file.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.thinkboot.common.exception.BusinessException;
import com.thinkboot.file.config.FileProperties;
import com.thinkboot.file.model.FileUploadResult;
import com.thinkboot.file.service.FileStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@ConditionalOnProperty(prefix = "thinkboot.file", name = "storage-type", havingValue = "oss")
public class OssFileStorageServiceImpl implements FileStorageService {

    private final FileProperties fileProperties;
    private final OSS ossClient;

    public OssFileStorageServiceImpl(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
        FileProperties.Oss ossConfig = fileProperties.getOss();
        this.ossClient = new OSSClientBuilder().build(
                ossConfig.getEndpoint(),
                ossConfig.getAccessKeyId(),
                ossConfig.getAccessKeySecret()
        );
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

        String originalFilename = file.getOriginalFilename();
        String extension = FileUtil.extName(originalFilename);
        
        List<String> allowedExtensions = Arrays.asList(fileProperties.getAllowedExtensions().split(","));
        if (!allowedExtensions.contains(extension.toLowerCase())) {
            throw new BusinessException("不支持的文件类型: " + extension);
        }

        String datePath = DateUtil.format(DateUtil.date(), "yyyyMMdd");
        String objectKey = (StrUtil.isNotBlank(directory) ? directory + "/" : "") + 
                           datePath + "/" + IdUtil.fastSimpleUUID() + "." + extension;

        try {
            ossClient.putObject(fileProperties.getOss().getBucketName(), objectKey, file.getInputStream());
        } catch (IOException e) {
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }

        String domain = StrUtil.isNotBlank(fileProperties.getOss().getDomain()) 
                ? fileProperties.getOss().getDomain() 
                : "https://" + fileProperties.getOss().getBucketName() + "." + fileProperties.getOss().getEndpoint();
        
        String fileUrl = domain + "/" + objectKey;

        FileUploadResult result = new FileUploadResult();
        result.setUrl(fileUrl);
        result.setName(originalFilename);
        result.setSize(file.getSize());
        result.setType(extension);
        
        return result;
    }

    @PreDestroy
    public void shutdown() {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }

    @Override
    public boolean delete(String fileUrl) {
        String domain = StrUtil.isNotBlank(fileProperties.getOss().getDomain()) 
                ? fileProperties.getOss().getDomain() 
                : "https://" + fileProperties.getOss().getBucketName() + "." + fileProperties.getOss().getEndpoint();
        
        String objectKey = fileUrl.replace(domain + "/", "");
        try {
            ossClient.deleteObject(fileProperties.getOss().getBucketName(), objectKey);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}