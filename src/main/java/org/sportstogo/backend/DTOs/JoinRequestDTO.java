package org.sportstogo.backend.DTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class JoinRequestDTO {
    private String id;
    private String displayName;
//    private String photoURL;
}
