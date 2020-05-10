package com.web.base.common.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dong
 * @date 2020/5/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebResponse<T> {

    private Integer code;

    private String message;

    private T data;

    public static <E> WebResponse<E> success(E data) {
        return new WebResponse<>(0, "SUCCESS", data);
    }

    public static <E> WebResponse<E> success(String message, E data) {
        return new WebResponse<>(0, message, data);
    }

    public static <E> WebResponse<E> fail(Integer code, String message, E data) {
        return new WebResponse<>(code, message, data);
    }

    public static <E> WebResponse<E> fail(Integer code, String message) {
        return new WebResponse<>(code, message, null);
    }

    public static <E> WebResponse<E> fail(String message, E data) {
        return new WebResponse<>(-1, message, data);
    }

}
