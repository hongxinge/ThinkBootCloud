package com.thinkboot.common.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageResponse<T> extends R<PageResponse.PageData<T>> {

    @Data
    public static class PageData<T> {
        private List<T> records;
        private long total;
        private long pages;
        private int pageNo;
        private int pageSize;

        public PageData() {
        }

        public PageData(List<T> records, long total, long pages, int pageNo, int pageSize) {
            this.records = records;
            this.total = total;
            this.pages = pages;
            this.pageNo = pageNo;
            this.pageSize = pageSize;
        }

        public boolean hasPrevious() {
            return pageNo > 1;
        }

        public boolean hasNext() {
            return pageNo < pages;
        }
    }

    public static <T> PageResponse<T> success(List<T> records, long total, long pages, int pageNo, int pageSize) {
        PageResponse<T> response = new PageResponse<>();
        response.setCode(R.SUCCESS);
        response.setMessage("success");
        response.setTimestamp(System.currentTimeMillis());

        PageData<T> pageData = new PageData<>(records, total, pages, pageNo, pageSize);
        response.setData(pageData);

        return response;
    }
}
