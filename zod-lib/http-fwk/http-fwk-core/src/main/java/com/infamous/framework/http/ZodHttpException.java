package com.infamous.framework.http;

public class ZodHttpException extends RuntimeException {

    public ZodHttpException(Exception e) {
        super(e);
    }

    public ZodHttpException(String mess) {
        super(mess);
    }

    public ZodHttpException(String s, Throwable cause) {
        super(s, cause);
    }
}
