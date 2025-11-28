package htw.webtech.myapp.rest;

import htw.webtech.myapp.business.service.ExternalExerciseApiService;
import htw.webtech.myapp.rest.model.ExternalExerciseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class ExternalExerciseController {

    private final ExternalExerciseApiService externalService;

    public ExternalExerciseController(ExternalExerciseApiService externalService) {
        this.externalService = externalService;
    }

    @GetMapping("/external/exercises")
    public ResponseEntity<List<ExternalExerciseDTO>> search(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String muscle
    ) {
        List<ExternalExerciseDTO> list = externalService.searchExercises(query, muscle);

        return list != null && !list.isEmpty()
                ? ResponseEntity.ok(list)
                : ResponseEntity.noContent().build();
    }
}
