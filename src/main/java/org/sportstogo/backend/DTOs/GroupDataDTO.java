package org.sportstogo.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class GroupDataDTO {
    private List<GroupMemberDTO> groupMembers;
    private List<JoinRequestDTO> joinRequests;
    private String name;
    private String description;
}
