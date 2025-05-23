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
    private int memberCount;
    private String description;
}
