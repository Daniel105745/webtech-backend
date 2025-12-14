package htw.webtech.myapp.rest;

import htw.webtech.myapp.business.service.TrainingPlanService;
import htw.webtech.myapp.rest.model.TrainingPlanDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// erlaubt NUR dein Frontend
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    public TrainingPlanController(TrainingPlanService trainingPlanService) {
        this.trainingPlanService = trainingPlanService;
    }

    @GetMapping("/plans")
    public List<TrainingPlanDTO> getAllPlans(@AuthenticationPrincipal Jwt token) {
        return trainingPlanService.getAllTrainingPlans(token.getSubject());
    }

    @PostMapping("/plans")
    public TrainingPlanDTO create(@RequestBody TrainingPlanDTO dto, @AuthenticationPrincipal Jwt token) {
        return trainingPlanService.create(dto, token.getSubject());
    }
    @DeleteMapping("/plans/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = trainingPlanService.delete(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
    @PutMapping("/plans/{id}")
    public ResponseEntity<TrainingPlanDTO> updatePlan(@PathVariable Long id, @RequestBody TrainingPlanDTO dto) {
        TrainingPlanDTO updated = trainingPlanService.update(id, dto);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    @GetMapping("/plans/{id}")
    public ResponseEntity<TrainingPlanDTO> getPlanById(@PathVariable Long id) {
        TrainingPlanDTO plan = trainingPlanService.getById(id);
        return plan != null
                ? ResponseEntity.ok(plan)
                : ResponseEntity.notFound().build();
    }

}