package com.web.syncspace.dto.message;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MessageDTO {

    @NotBlank(message = "Sender id is required")
    private String senderUserName;

    @NotBlank(message = "Receiver id is required")
    private String receiverUserName;

    @NotBlank(message = "Content is required")
    @Size(min = 1, max = 1000, message = "Content must be between 1 and 1000 characters")
    private String content;

}
