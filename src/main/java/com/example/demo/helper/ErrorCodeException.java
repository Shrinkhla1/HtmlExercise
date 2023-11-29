package com.example.demo.helper;

import com.example.demo.model.ErrorCode;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.util.Properties;

public class ErrorCodeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    private final ErrorCode errorCode;

    public ErrorCodeException(ErrorCode errorCode) {
        super(getErrorMessage(errorCode));
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    private static String getErrorMessage(ErrorCode errorCode) {
        Properties properties = new Properties();
        String errorMessage = null;

        try (InputStream input = ErrorCodeException.class.getClassLoader().getResourceAsStream("error-messages.properties")) {
            properties.load(input);
            errorMessage = properties.getProperty(errorCode.name());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return errorMessage != null ? errorMessage : "Unknown error";
    }
}
