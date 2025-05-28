package org.sportstogo.backend.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sportstogo.backend.Enums.ReportTargetType;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReportInfoDTO {
    private String id;
    private Integer reports;
    private ReportTargetType type;
}
