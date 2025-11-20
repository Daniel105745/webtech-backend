package htw.webtech.myapp.business.service;

import htw.webtech.myapp.rest.model.ExternalExerciseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Service
public class ExternalExerciseApiService {

    @Value("${api.ninjas.key}")
    private String apiKey;

    private final RestTemplate rest = new RestTemplate();

    public List<ExternalExerciseDTO> searchExercises(String query, String muscle) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl("https://api.api-ninjas.com/v1/exercises");

        if (query != null && !query.isBlank()) {
            builder.queryParam("name", query);
        }

        if (muscle != null && !muscle.isBlank()) {
            builder.queryParam("muscle", muscle);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", apiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ExternalExerciseDTO[]> response =
                rest.exchange(builder.toUriString(), HttpMethod.GET, entity, ExternalExerciseDTO[].class);

        ExternalExerciseDTO[] body = response.getBody();

        return body != null
                ? Arrays.asList(body)
                : List.of();
    }
}
