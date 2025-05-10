package org.sportstogo.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.Enums.Role;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class GroupMemberDTO {
    private String displayName;
    private String id;
    private Role role;
//    private String photoUrl;
}
