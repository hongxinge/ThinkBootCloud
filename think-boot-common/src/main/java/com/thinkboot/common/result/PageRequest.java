package com.thinkboot.common.result;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class PageRequest {

    @Min(value = 1, message = "Page number must be greater than 0")
    private int pageNo = 1;

    @Min(value = 1, message = "Page size must be greater than 0")
    private int pageSize = 10;

    private String orderBy;

    private String orderDirection = "ASC";

    public int getPageNo() {
        return pageNo < 1 ? 1 : pageNo;
    }

    public int getPageSize() {
        return pageSize < 1 ? 10 : (pageSize > 100 ? 100 : pageSize);
    }
}
