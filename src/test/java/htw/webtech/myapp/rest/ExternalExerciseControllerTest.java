package htw.webtech.myapp.rest;

import htw.webtech.myapp.business.service.ExternalExerciseApiService;
import htw.webtech.myapp.rest.model.ExternalExerciseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class ExternalExerciseControllerTest {

    @Mock
    private ExternalExerciseApiService externalService;

    @InjectMocks
    private ExternalExerciseController controller;

    private ExternalExerciseDTO dto;

    @BeforeEach
    void setUp() {
        dto = new ExternalExerciseDTO();
        dto.setName("Push-up");
        dto.setType("strength");
        dto.setMuscle("chest");
        dto.setEquipment("none");
        dto.setDifficulty("beginner");
        dto.setInstructions("Keep your body straight and lower yourself.");
    }

    @Test
    void testSearch_Returns200_WhenListNotEmpty() {
        // GIVEN
        Mockito.when(externalService.searchExercises(eq("push"), eq("chest")))
                .thenReturn(List.of(dto));

        // WHEN
        ResponseEntity<List<ExternalExerciseDTO>> response =
                controller.search("push", "chest");

        // THEN
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Push-up");
    }

    @Test
    void testSearch_Returns204_WhenListEmpty() {
        Mockito.when(externalService.searchExercises(any(), any()))
                .thenReturn(List.of());

        ResponseEntity<List<ExternalExerciseDTO>> response =
                controller.search(null, null);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void testSearch_Returns204_WhenListNull() {
        Mockito.when(externalService.searchExercises(any(), any()))
                .thenReturn(null);

        ResponseEntity<List<ExternalExerciseDTO>> response =
                controller.search(null, null);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        assertThat(response.getBody()).isNull();
    }
}
