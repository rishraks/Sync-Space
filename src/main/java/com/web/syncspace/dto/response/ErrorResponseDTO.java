package com.web.syncspace.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {

    private String apiUri;

    private HttpStatus httpStatus;

    private String errorMessage;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime timestamp = OffsetDateTime.now(ZoneOffset.UTC);

}
