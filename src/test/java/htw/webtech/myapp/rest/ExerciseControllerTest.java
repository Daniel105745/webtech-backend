package htw.webtech.myapp.rest;

import htw.webtech.myapp.business.service.ExerciseService;
import htw.webtech.myapp.rest.model.ExerciseDTO;
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
class ExerciseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExerciseService exerciseService;

    @BeforeEach
    void setup() {
        ExerciseController controller = new ExerciseController(exerciseService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // -------------------------------------------------------
    // 1. GET exercises by workout
    // -------------------------------------------------------
    @Test
    void testGetByWorkout() throws Exception {
        ExerciseDTO dto1 = new ExerciseDTO(1L, "Pushups", 3, 15, 0.0, 1L);
        ExerciseDTO dto2 = new ExerciseDTO(2L, "Pullups", 4, 12, 0.0, 1L);

        when(exerciseService.getExercisesByWorkout(1L)).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/exercises/workout/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pushups"))
                .andExpect(jsonPath("$[1].name").value("Pullups"));
    }

    // -------------------------------------------------------
    // 2. CREATE exercise
    // -------------------------------------------------------
    @Test
    void testCreateExercise() throws Exception {
        ExerciseDTO dto = new ExerciseDTO(null, "Squats", 4, 12, 20.0, 1L);
        ExerciseDTO created = new ExerciseDTO(3L, "Squats", 4, 12, 20.0, 1L);

        when(exerciseService.createExercise(Mockito.any(), Mockito.eq(1L))).thenReturn(created);

        mockMvc.perform(post("/exercises/workout/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id":null,
                                  "name":"Squats",
                                  "saetze":4,
                                  "wiederholungen":12,
                                  "gewicht":20.0,
                                  "workoutId":1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Squats"));
    }

    // -------------------------------------------------------
    // 3. UPDATE exercise success
    // -------------------------------------------------------
    @Test
    void testUpdateExercise_Success() throws Exception {
        ExerciseDTO dto = new ExerciseDTO(1L, "ModifiedPushups", 5, 20, 0.0, 1L);

        when(exerciseService.updateExercise(Mockito.eq(1L), Mockito.any())).thenReturn(dto);

        mockMvc.perform(put("/exercises/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id":1,
                                  "name":"ModifiedPushups",
                                  "saetze":5,
                                  "wiederholungen":20,
                                  "gewicht":0.0,
                                  "workoutId":1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ModifiedPushups"))
                .andExpect(jsonPath("$.saetze").value(5));
    }

    // -------------------------------------------------------
    // 4. UPDATE exercise not found
    // -------------------------------------------------------
    @Test
    void testUpdateExercise_NotFound() throws Exception {
        when(exerciseService.updateExercise(Mockito.eq(99L), Mockito.any())).thenReturn(null);

        mockMvc.perform(put("/exercises/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id":99,
                                  "name":"X",
                                  "saetze":1,
                                  "wiederholungen":1,
                                  "gewicht":0.0,
                                  "workoutId":1
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------
    // 5. DELETE exercise success
    // -------------------------------------------------------
    @Test
    void testDeleteExercise_Success() throws Exception {
        when(exerciseService.deleteExercise(1L)).thenReturn(true);

        mockMvc.perform(delete("/exercises/1"))
                .andExpect(status().isNoContent());
    }

    // -------------------------------------------------------
    // 6. DELETE exercise not found
    // -------------------------------------------------------
    @Test
    void testDeleteExercise_NotFound() throws Exception {
        when(exerciseService.deleteExercise(99L)).thenReturn(false);

        mockMvc.perform(delete("/exercises/99"))
                .andExpect(status().isNotFound());
    }
}
