package htw.webtech.myapp.rest.controller;

import htw.webtech.myapp.business.service.ExerciseService;
import htw.webtech.myapp.rest.model.ExerciseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://webtech-frontend-fe2m.onrender.com"
})
@RequestMapping("/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    // ====================================================
    // INTERN: Exercises im Workout
    // ====================================================

    @GetMapping("/workout/{workoutId}")
    public List<ExerciseDTO> getByWorkout(@PathVariable Long workoutId) {
        return exerciseService.getExercisesByWorkout(workoutId);
    }

    @PostMapping("/workout/{workoutId}")
    public ExerciseDTO create(@PathVariable Long workoutId, @RequestBody ExerciseDTO dto) {
        return exerciseService.createExercise(dto, workoutId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseDTO> update(@PathVariable Long id, @RequestBody ExerciseDTO dto) {
        ExerciseDTO updated = exerciseService.updateExercise(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = exerciseService.deleteExercise(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }


}
