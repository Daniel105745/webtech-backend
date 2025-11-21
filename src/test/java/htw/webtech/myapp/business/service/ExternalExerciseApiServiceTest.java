package htw.webtech.myapp.business.service;

import htw.webtech.myapp.rest.model.ExternalExerciseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExternalExerciseApiServiceTest {

    @InjectMocks
    private ExternalExerciseApiService service;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Gán RestTemplate đã mock vào biến private "rest" bằng Reflection
        try {
            var field = ExternalExerciseApiService.class.getDeclaredField("rest");
            field.setAccessible(true);
            field.set(service, restTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Gán API KEY
        try {
            var field = ExternalExerciseApiService.class.getDeclaredField("apiKey");
            field.setAccessible(true);
            field.set(service, "TEST_API_KEY");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSearchExercises_withQueryAndMuscle() {
        // GIVEN
        String query = "pushup";
        String muscle = "chest";

        ExternalExerciseDTO dto = new ExternalExerciseDTO();
        dto.setName("Push-up");
        ExternalExerciseDTO[] responseBody = new ExternalExerciseDTO[]{dto};

        ResponseEntity<ExternalExerciseDTO[]> mockResponse =
                new ResponseEntity<>(responseBody, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(ExternalExerciseDTO[].class)
        )).thenReturn(mockResponse);

        // WHEN
        List<ExternalExerciseDTO> result = service.searchExercises(query, muscle);

        // THEN
        assertEquals(1, result.size());
        assertEquals("Push-up", result.get(0).getName());

        // Verify URL được build đúng
        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        verify(restTemplate).exchange(
                urlCaptor.capture(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(ExternalExerciseDTO[].class)
        );

        String url = urlCaptor.getValue();
        assertTrue(url.contains("name=pushup"));
        assertTrue(url.contains("muscle=chest"));
    }

    @Test
    void testSearchExercises_nullResponseBody_returnsEmptyList() {
        // Mock response body = null
        ResponseEntity<ExternalExerciseDTO[]> mockResponse =
                new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), any(), any(), eq(ExternalExerciseDTO[].class)))
                .thenReturn(mockResponse);

        List<ExternalExerciseDTO> result = service.searchExercises(null, null);

        assertTrue(result.isEmpty());
    }

}
