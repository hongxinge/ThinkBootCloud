package com.thinkboot.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {

    public static final int SUCCESS = 200;
    public static final int ERROR = 500;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;

    private int code;
    private String message;
    private T data;
    private long timestamp;

    public static <T> R<T> success() {
        return success(null);
    }

    public static <T> R<T> success(T data) {
        R<T> r = new R<>();
        r.setCode(SUCCESS);
        r.setMessage("success");
        r.setData(data);
        r.setTimestamp(System.currentTimeMillis());
        return r;
    }

    public static <T> R<T> error(int code, String message) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMessage(message);
        r.setTimestamp(System.currentTimeMillis());
        return r;
    }

    public static <T> R<T> error(String message) {
        return error(ERROR, message);
    }
}
