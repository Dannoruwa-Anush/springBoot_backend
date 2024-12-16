package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // Generate Getter & Setters using Lombok
public class MessageResponseDTO {
    // DTO specifies exactly what fields should be serialized.
    private String message;
}
