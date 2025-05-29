package org.sportstogo.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.Enums.Theme;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class GroupDataDTO extends GroupPreviewDTO {
    private List<GroupMemberDTO> groupMembers = new ArrayList<>();
    private List<JoinRequestDTO> joinRequests = new ArrayList<>();
    private Theme theme;
    GroupDataDTO(Long id, String name, String description,  Short theme) {
        super(id, name, description);
        this.theme = Theme.values()[theme];
    }
}
