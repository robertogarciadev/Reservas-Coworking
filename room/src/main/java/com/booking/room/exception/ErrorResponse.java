package com.booking.room.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.boot.models.annotations.internal.TimeZoneStorageAnnotation;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponse {
    private LocalDate timeStamp;
    private String message;
    private String details;
    private int code;
}
