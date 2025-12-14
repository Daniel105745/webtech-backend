package htw.webtech.myapp.rest;

import htw.webtech.myapp.business.service.WorkoutService;
import htw.webtech.myapp.rest.controller.WorkoutController;
import htw.webtech.myapp.rest.model.WorkoutDTO;
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
class WorkoutControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WorkoutService workoutService;

    @BeforeEach
    void setup() {
        WorkoutController controller = new WorkoutController(workoutService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // -------------------------------------------------------
    // 1. GET all workouts by training plan
    // -------------------------------------------------------
    @Test
    void testGetAllByTrainingPlan() throws Exception {
        WorkoutDTO dto1 = new WorkoutDTO(1L, "Workout 1", "Monday", "Chest", 1L);
        WorkoutDTO dto2 = new WorkoutDTO(2L, "Workout 2", "Tuesday", "Back", 1L);

        when(workoutService.getAllByTrainingPlan(1L)).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/workouts/plan/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Workout 1"))
                .andExpect(jsonPath("$[1].name").value("Workout 2"));
    }

    // -------------------------------------------------------
    // 2. GET workout by id FOUND
    // -------------------------------------------------------
    @Test
    void testGetWorkoutFound() throws Exception {
        WorkoutDTO dto = new WorkoutDTO(1L, "Workout 1", "Monday", "Chest", 1L);

        when(workoutService.getWorkoutById(1L)).thenReturn(dto);

        mockMvc.perform(get("/workouts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Workout 1"))
                .andExpect(jsonPath("$.muskelgruppe").value("Chest"));
    }

    // -------------------------------------------------------
    // 3. GET workout by id NOT FOUND
    // -------------------------------------------------------
    @Test
    void testGetWorkoutNotFound() throws Exception {
        when(workoutService.getWorkoutById(99L)).thenReturn(null);

        mockMvc.perform(get("/workouts/99"))
                .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------
    // 4. CREATE workout
    // -------------------------------------------------------
    @Test
    void testCreateWorkout() throws Exception {
        WorkoutDTO dto = new WorkoutDTO(null, "Workout 2", "Tuesday", "Back", 1L);
        WorkoutDTO created = new WorkoutDTO(2L, "Workout 2", "Tuesday", "Back", 1L);

        when(workoutService.createWorkout(Mockito.eq(1L), Mockito.any())).thenReturn(created);

        mockMvc.perform(post("/workouts/plan/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": null,
                                  "name": "Workout 2",
                                  "dayOfWeek": "Tuesday",
                                  "muskelgruppe": "Back",
                                  "trainingPlanId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Workout 2"));
    }

    // -------------------------------------------------------
    // 5. UPDATE workout FOUND
    // -------------------------------------------------------
    @Test
    void testUpdateWorkoutFound() throws Exception {
        WorkoutDTO dto = new WorkoutDTO(1L, "Workout 1 Modified", "Monday", "Chest", 1L);

        when(workoutService.updateWorkout(Mockito.eq(1L), Mockito.any())).thenReturn(dto);

        mockMvc.perform(put("/workouts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 1,
                                  "name": "Workout 1 Modified",
                                  "dayOfWeek": "Monday",
                                  "muskelgruppe": "Chest",
                                  "trainingPlanId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Workout 1 Modified"))
                .andExpect(jsonPath("$.muskelgruppe").value("Chest"));
    }

    // -------------------------------------------------------
    // 6. UPDATE workout NOT FOUND
    // -------------------------------------------------------
    @Test
    void testUpdateWorkoutNotFound() throws Exception {
        when(workoutService.updateWorkout(Mockito.eq(99L), Mockito.any())).thenReturn(null);

        mockMvc.perform(put("/workouts/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 99,
                                  "name": "X",
                                  "dayOfWeek": "Friday",
                                  "muskelgruppe": "Legs",
                                  "trainingPlanId": 1
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------
    // 7. DELETE workout SUCCESS
    // -------------------------------------------------------
    @Test
    void testDeleteWorkoutSuccess() throws Exception {
        when(workoutService.deleteWorkout(1L)).thenReturn(true);

        mockMvc.perform(delete("/workouts/1"))
                .andExpect(status().isNoContent());
    }

    // -------------------------------------------------------
    // 8. DELETE workout NOT FOUND
    // -------------------------------------------------------
    @Test
    void testDeleteWorkoutNotFound() throws Exception {
        when(workoutService.deleteWorkout(99L)).thenReturn(false);

        mockMvc.perform(delete("/workouts/99"))
                .andExpect(status().isNotFound());
    }
}
