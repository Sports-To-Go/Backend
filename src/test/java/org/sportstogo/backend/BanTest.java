package org.sportstogo.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.sportstogo.backend.Controller.BanController;
import org.sportstogo.backend.Models.Ban;
import org.sportstogo.backend.Service.BanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;


@WebMvcTest(BanController.class)
@AutoConfigureMockMvc(addFilters = false) // This disables Spring Security filters
class BanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BanService banService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllBans() throws Exception {
        Ban ban = new Ban();
        ban.setId(1L);
        ban.setIdUser("dsfjn");
        ban.setDuration(7);
        ban.setReason("Violation");

        when(banService.getBans()).thenReturn(List.of(ban));

        mockMvc.perform(get("/administration/bans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void shouldAddBan() throws Exception {
        Ban ban = new Ban();
        ban.setIdUser("dwf");
        ban.setDuration(5);
        ban.setReason("Abuse");

        String json = objectMapper.writeValueAsString(ban);

        mockMvc.perform(post("/administration/bans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("added successful")));

        verify(banService, times(1)).addBan(any(Ban.class));
    }

    @Test
    void shouldUpdateBan() throws Exception {
        mockMvc.perform(put("/administration/bans/1")
                        .param("duration", "10")
                        .param("reason", "Extended ban"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("updated successfully")));

        verify(banService).updateBan(1L, 10, "Extended ban");
    }

    @Test
    void shouldDeleteBan() throws Exception {
        mockMvc.perform(delete("/administration/bans/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("deleted successfully")));

        verify(banService).deleteBan(1L);
    }
}
