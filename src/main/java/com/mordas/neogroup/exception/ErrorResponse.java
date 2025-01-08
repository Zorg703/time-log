package com.mordas.neogroup.exception;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class ErrorResponse {
    private int statusCode;
    @NonNull
    private String message;
}
