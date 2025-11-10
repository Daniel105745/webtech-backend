package htw.webtech.myapp.rest.controller;

import htw.webtech.myapp.business.service.WorkoutService;
import htw.webtech.myapp.rest.model.WorkoutDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://webtech-frontend-fe2m.onrender.com"
})
@RequestMapping("/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping("/plan/{planId}")
    public List<WorkoutDTO> getAllByTrainingPlan(@PathVariable Long planId) {
        return workoutService.getAllByTrainingPlan(planId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutDTO> getWorkout(@PathVariable Long id) {
        WorkoutDTO dto = workoutService.getWorkoutById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    // JSON akzeptieren
    @PostMapping(value = "/plan/{planId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<WorkoutDTO> create(@PathVariable Long planId, @RequestBody WorkoutDTO dto) {
        WorkoutDTO created = workoutService.createWorkout(planId, dto);
        return ResponseEntity.ok(created);
    }

    // JSON akzeptieren
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<WorkoutDTO> update(@PathVariable Long id, @RequestBody WorkoutDTO dto) {
        WorkoutDTO updated = workoutService.updateWorkout(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = workoutService.deleteWorkout(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
