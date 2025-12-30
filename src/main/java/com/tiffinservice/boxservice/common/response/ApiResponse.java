package com.tiffinservice.boxservice.common.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiResponse<T> {
    private boolean success;
    private int status;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private T data;
}
