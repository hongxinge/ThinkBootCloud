package com.thinkboot.file.model;

import lombok.Data;

@Data
public class FileUploadResult {
    private String url;
    private String name;
    private Long size;
    private String type;
}