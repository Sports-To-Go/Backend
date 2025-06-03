package org.sportstogo.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class GroupPreviewDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;

    public GroupPreviewDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description != null ? description : "No description";
    }
}
