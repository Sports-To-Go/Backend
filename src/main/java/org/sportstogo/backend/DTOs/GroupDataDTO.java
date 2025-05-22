package org.sportstogo.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Setter @Getter
public class GroupDataDTO extends GroupPreviewDTO {
    private List<GroupMemberDTO> groupMembers = new ArrayList<>();
    private List<JoinRequestDTO> joinRequests = new ArrayList<>();

    GroupDataDTO(Long id, String name, String description) {
        super(id, name, description);
    }
}
