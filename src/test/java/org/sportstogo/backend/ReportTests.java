package org.sportstogo.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.sportstogo.backend.Controller.ReportController;
import org.sportstogo.backend.Models.Report;
import org.sportstogo.backend.Models.ReportStatus;
import org.sportstogo.backend.Models.ReportTargetType;
import org.sportstogo.backend.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllReports() throws Exception {
        Report report = new Report();
        report.setId(1L);
        report.setReason("Spam");
        report.setTargetType(ReportTargetType.User);
        report.setTargetId(10L);

        when(reportService.getReports()).thenReturn(List.of(report));

        mockMvc.perform(get("/administration/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void shouldAddReport() throws Exception {
        Report report = new Report();
        report.setReportedBy(2L);
        report.setTargetType(ReportTargetType.User);
        report.setTargetId(10L);
        report.setReason("Inappropriate behavior");
        report.setStatus(ReportStatus.Open);

        String json = objectMapper.writeValueAsString(report);

        mockMvc.perform(post("/administration/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("added successful")));

        verify(reportService, times(1)).addReport(any(Report.class));
    }

    @Test
    void shouldUpdateReport() throws Exception {
        mockMvc.perform(put("/administration/reports/1")
                        .param("reviewedBy", "99")
                        .param("reviewedAt", "2025-05-01")
                        .param("status", "RESOLVED"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("updated successfuly")));

        verify(reportService).updateReport(1L, 99L, LocalDate.of(2025, 5, 1), ReportStatus.Resolved);
    }

    @Test
    void shouldDeleteReportById() throws Exception {
        mockMvc.perform(delete("/administration/reports/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("deleted successfully")));

        verify(reportService).deleteReport(1L);
    }

    @Test
    void shouldDeleteReportsByTargetIdAndType() throws Exception {
        Report report1 = new Report();
        report1.setId(1L);
        report1.setTargetType(ReportTargetType.User);
        report1.setTargetId(10L);

        when(reportService.getReports()).thenReturn(List.of(report1));

        mockMvc.perform(delete("/administration/reports/USER/10"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("deleted successfully")));

        verify(reportService).deleteReport(1L);
    }
}
