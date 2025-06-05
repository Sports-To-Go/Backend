package org.sportstogo.backend.DTOs;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NameDTO {
    private final String name;
    private String imageUrl;
}
