package com.jkm.controller.helper;

/**
 * Created by lk on 9/20/16.
 */
public class ResponseEntityBase<T> {
    private int code = 1;

    private String message = "success";

    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
