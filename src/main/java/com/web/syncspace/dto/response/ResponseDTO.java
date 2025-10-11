package com.web.syncspace.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {

    private String statusCode;

    private String statusMessage;

    private T data;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String additionalData;
}
