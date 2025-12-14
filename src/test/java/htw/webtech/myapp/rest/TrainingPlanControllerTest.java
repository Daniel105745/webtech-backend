package htw.webtech.myapp.rest;

import htw.webtech.myapp.business.service.TrainingPlanService;
import htw.webtech.myapp.rest.controller.TrainingPlanController;
import htw.webtech.myapp.rest.model.TrainingPlanDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TrainingPlanControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TrainingPlanService trainingPlanService;

    @BeforeEach
    void setup() {
        TrainingPlanController controller = new TrainingPlanController(trainingPlanService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // -------------------------------------------------------
    // 1. GET ALL
    // -------------------------------------------------------
    @Test
    void testGetAllPlans() throws Exception {
        List<TrainingPlanDTO> mockList = List.of(
                new TrainingPlanDTO(1, "Plan A", "30 min", "Mittel", "Beine"),
                new TrainingPlanDTO(2, "Plan B", "45 min", "Hoch", "Rücken")
        );

        when(trainingPlanService.getAllTrainingPlans()).thenReturn(mockList);

        mockMvc.perform(get("/plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Plan A"))
                .andExpect(jsonPath("$[1].dauer").value("45 min"));
    }

    // -------------------------------------------------------
    // 2. GET BY ID (FOUND)
    // -------------------------------------------------------
    @Test
    void testGetPlanById_Found() throws Exception {
        TrainingPlanDTO dto = new TrainingPlanDTO(1, "Test", "30", "Hoch", "Brust");

        when(trainingPlanService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/plans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"));
    }

    // -------------------------------------------------------
    // 3. GET BY ID (NOT FOUND)
    // -------------------------------------------------------
    @Test
    void testGetPlanById_NotFound() throws Exception {
        when(trainingPlanService.getById(99L)).thenReturn(null);

        mockMvc.perform(get("/plans/99"))
                .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------
    // 4. CREATE
    // -------------------------------------------------------
    @Test
    void testCreatePlan() throws Exception {
        TrainingPlanDTO dto =
                new TrainingPlanDTO(0, "New", "20", "Low", "Arme");

        when(trainingPlanService.create(Mockito.any())).thenReturn(dto);

        mockMvc.perform(post("/plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id":0,
                                  "name":"New",
                                  "dauer":"20",
                                  "intensitaet":"Low",
                                  "zielmuskeln":"Arme"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New"));
    }

    // -------------------------------------------------------
    // 5. UPDATE (FOUND)
    // -------------------------------------------------------
    @Test
    void testUpdatePlan_Found() throws Exception {
        TrainingPlanDTO updated =
                new TrainingPlanDTO(1, "Updated", "25", "Mittel", "Beine");

        when(trainingPlanService.update(Mockito.eq(1L), Mockito.any()))
                .thenReturn(updated);

        mockMvc.perform(put("/plans/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id":1,
                                  "name":"Updated",
                                  "dauer":"25",
                                  "intensitaet":"Mittel",
                                  "zielmuskeln":"Beine"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    // -------------------------------------------------------
    // 6. UPDATE (NOT FOUND)
    // -------------------------------------------------------
    @Test
    void testUpdatePlan_NotFound() throws Exception {
        when(trainingPlanService.update(Mockito.eq(99L), Mockito.any()))
                .thenReturn(null);

        mockMvc.perform(put("/plans/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id":99,
                                  "name":"X",
                                  "dauer":"0",
                                  "intensitaet":"Low",
                                  "zielmuskeln":"None"
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------
    // 7. DELETE (FOUND)
    // -------------------------------------------------------
    @Test
    void testDeletePlan_Found() throws Exception {
        when(trainingPlanService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/plans/1"))
                .andExpect(status().isNoContent());
    }

    // -------------------------------------------------------
    // 8. DELETE (NOT FOUND)
    // -------------------------------------------------------
    @Test
    void testDeletePlan_NotFound() throws Exception {
        when(trainingPlanService.delete(99L)).thenReturn(false);

        mockMvc.perform(delete("/plans/99"))
                .andExpect(status().isNotFound());
    }
}
