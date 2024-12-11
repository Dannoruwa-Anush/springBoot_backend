package com.example.demo.dto.response;

import lombok.Data;

@Data // Generate Getter & Setters using Lombok
public class MessageResponseDTO {
    private String message;

    // ---
    public MessageResponseDTO(String message) {
        this.message = message;
    }
    // ---
}
