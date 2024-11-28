package com.ojw.planner.core.response;

import lombok.Data;

@Data
public class APIResponse<T>  {

    private boolean result;

    private String message;
    private T data;

    public APIResponse(String message) {
        this(message, null);
    }

    public APIResponse(T data) {
        this("success", data);
    }

    public APIResponse(boolean result, String message) {
        this(result, message, null);
    }

    public APIResponse(String message, T data) {
        this(true, message, data);
    }

    public APIResponse(boolean result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

}
