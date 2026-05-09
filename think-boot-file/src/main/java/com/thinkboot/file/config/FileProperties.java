package com.thinkboot.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "thinkboot.file")
public class FileProperties {
    private String storageType = "local";
    private String localPath = "/data/files";
    private String baseUrl = "http://localhost:8080/files";
    private String allowedExtensions = "jpg,jpeg,png,gif,bmp,pdf,doc,docx,xls,xlsx,ppt,pptx,txt,zip,rar,mp4,mp3";
    private int maxFileSize = 10;

    private Oss oss = new Oss();

    @Data
    public static class Oss {
        private String endpoint;
        private String accessKeyId;
        private String accessKeySecret;
        private String bucketName;
        private String domain;
    }
}