package com.ojw.planner.core.response;

import lombok.Data;

@Data
public class ApiResponse<T>  {

    private boolean result;

    private String message;
    private T data;

    public ApiResponse(String message) {
        this(message, null);
    }

    public ApiResponse(T data) {
        this("success", data);
    }

    public ApiResponse(boolean result, String message) {
        this(result, message, null);
    }

    public ApiResponse(String message, T data) {
        this(true, message, data);
    }

    public ApiResponse(boolean result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

}
