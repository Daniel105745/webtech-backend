package htw.webtech.myapp.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import htw.webtech.myapp.business.service.TrainingPlanService;
import htw.webtech.myapp.rest.model.TrainingPlanDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainingPlanController.class)
class TrainingPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrainingPlanService trainingPlanService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------- GET /plans ----------
    @Test
    void getAllPlans_shouldReturnList() throws Exception {
        TrainingPlanDTO dto = new TrainingPlanDTO(
                1,
                "Push Day",
                "60",
                "hoch",
                "Muskelaufbau"
        );

        Mockito.when(trainingPlanService.getAllTrainingPlans("user123"))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/plans")
                        .with(jwt().jwt(jwt -> jwt.subject("user123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Push Day"));
    }

    // ---------- POST /plans ----------
    @Test
    void createPlan_shouldReturnCreatedPlan() throws Exception {
        TrainingPlanDTO input = new TrainingPlanDTO(
                0,
                "Leg Day",
                "45",
                "mittel",
                "Beine"
        );

        TrainingPlanDTO saved = new TrainingPlanDTO(
                1,
                "Leg Day",
                "45",
                "mittel",
                "Beine"
        );

        Mockito.when(trainingPlanService.create(Mockito.any(), Mockito.eq("user123")))
                .thenReturn(saved);

        mockMvc.perform(post("/plans")
                        .with(jwt().jwt(jwt -> jwt.subject("user123")))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Leg Day"));
    }

    // ---------- GET /plans/{id} ----------
    @Test
    void getPlanById_shouldReturnPlan() throws Exception {
        TrainingPlanDTO dto = new TrainingPlanDTO(
                1,
                "Pull Day",
                "50",
                "hoch",
                "Ruecken"
        );

        Mockito.when(trainingPlanService.getById(1L))
                .thenReturn(dto);

        mockMvc.perform(get("/plans/1")
                        .with(jwt().jwt(jwt -> jwt.subject("user123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pull Day"));
    }

    @Test
    void getPlanById_shouldReturn404_whenNotFound() throws Exception {
        Mockito.when(trainingPlanService.getById(99L))
                .thenReturn(null);

        mockMvc.perform(get("/plans/99")
                        .with(jwt().jwt(jwt -> jwt.subject("user123"))))
                .andExpect(status().isNotFound());
    }

    // ---------- PUT /plans/{id} ----------
    @Test
    void updatePlan_shouldReturnUpdatedPlan() throws Exception {
        TrainingPlanDTO input = new TrainingPlanDTO(
                0,
                "Updated Plan",
                "70",
                "niedrig",
                "Ausdauer"
        );

        TrainingPlanDTO updated = new TrainingPlanDTO(
                1,
                "Updated Plan",
                "70",
                "niedrig",
                "Ausdauer"
        );

        Mockito.when(trainingPlanService.update(Mockito.eq(1L), Mockito.any()))
                .thenReturn(updated);

        mockMvc.perform(put("/plans/1")
                        .with(jwt().jwt(jwt -> jwt.subject("user123")))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Plan"))
                .andExpect(jsonPath("$.dauer").value("70"));
    }

    @Test
    void updatePlan_shouldReturn404_whenNotFound() throws Exception {
        Mockito.when(trainingPlanService.update(Mockito.eq(99L), Mockito.any()))
                .thenReturn(null);

        mockMvc.perform(put("/plans/99")
                        .with(jwt().jwt(jwt -> jwt.subject("user123")))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new TrainingPlanDTO(0, "", "", "", "")
                        )))
                .andExpect(status().isNotFound());
    }

    // ---------- DELETE /plans/{id} ----------
    @Test
    void deletePlan_shouldReturn204() throws Exception {
        Mockito.when(trainingPlanService.delete(1L))
                .thenReturn(true);

        mockMvc.perform(delete("/plans/1")
                        .with(jwt().jwt(jwt -> jwt.subject("user123")))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePlan_shouldReturn404() throws Exception {
        Mockito.when(trainingPlanService.delete(99L))
                .thenReturn(false);

        mockMvc.perform(delete("/plans/99")
                        .with(jwt().jwt(jwt -> jwt.subject("user123")))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
