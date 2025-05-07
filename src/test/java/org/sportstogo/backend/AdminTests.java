package org.sportstogo.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.sportstogo.backend.Controller.AdminController;
import org.sportstogo.backend.Models.Revenue;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminService adminService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("GET /admin/recent-users")
    class RecentUsersTests {

        @Test
        @DisplayName("când nu sunt utilizatori, returnează 204 No Content")
        void whenNoUsers_thenNoContent() throws Exception {
            given(adminService.getUsersRegisteredLastWeek()).willReturn(Collections.emptyList());

            mockMvc.perform(get("/admin/recent-users"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("când sunt utilizatori, returnează 200 OK și lista de users")
        void whenUsersExist_thenOkAndJsonList() throws Exception {
            User u = new User();
            u.setUid("u1");
            u.setDescription("descr");
            given(adminService.getUsersRegisteredLastWeek()).willReturn(List.of(u));

            mockMvc.perform(get("/admin/recent-users"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].uid", is("u1")))
                    .andExpect(jsonPath("$[0].description", is("descr")));
        }
    }

    @Nested
    @DisplayName("GET count endpoints")
    class CountTests {

        @Test
        void locationsCount_thenReturnOkAndNumber() throws Exception {
            given(adminService.getLocationCount()).willReturn(42L);

            mockMvc.perform(get("/admin/locations/count"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("42"));
        }

        @Test
        void reservationCount_thenReturnOkAndNumber() throws Exception {
            given(adminService.getReservationCount()).willReturn(17L);

            mockMvc.perform(get("/admin/reservations/count"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("17"));
        }

        @Test
        void recentUsersCount_thenReturnOkAndNumber() throws Exception {
            given(adminService.getNumberOfUsersRegisteredInLastWeek()).willReturn(5L);

            mockMvc.perform(get("/admin/recent-users/count"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("5"));
        }
    }

    @Nested
    @DisplayName("GET /admin/revenue/annual")
    class AnnualRevenueTests {

        private final LocalDate from = LocalDate.of(2025,1,1);
        private final LocalDate to   = LocalDate.of(2025,5,4);

        @Test
        @DisplayName("când lipsesc parametrii, returnează 400 Bad Request")
        void whenMissingParams_thenBadRequest() throws Exception {
            mockMvc.perform(get("/admin/revenue/annual"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("când nu sunt revenue, returnează 204 No Content")
        void whenEmpty_thenNoContent() throws Exception {
            given(adminService.getAnnualRevenue(from, to)).willReturn(Collections.emptyList());

            mockMvc.perform(get("/admin/revenue/annual")
                            .param("from", from.toString())
                            .param("to",   to.toString()))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("când există revenue, returnează 200 OK și lista de revenue")
        void whenRevenueExist_thenOkAndJsonList() throws Exception {
            Revenue r = new Revenue();
            r.setId(1L);
            r.setPeriodType(Revenue.PeriodType.ANNUAL);
            r.setPeriodStart(from);
            r.setTotalAmount(1234.56);
            given(adminService.getAnnualRevenue(from, to)).willReturn(List.of(r));

            mockMvc.perform(get("/admin/revenue/annual")
                            .param("from", from.toString())
                            .param("to",   to.toString()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].periodType", is("ANNUAL")))
                    .andExpect(jsonPath("$[0].periodStart", is(from.toString())))
                    .andExpect(jsonPath("$[0].totalAmount", is(1234.56)));
        }
    }
}
