package htw.webtech.myapp.rest.controller;

import htw.webtech.myapp.business.service.TrainingPlanService;
import htw.webtech.myapp.rest.model.TrainingPlanDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    public TrainingPlanController(TrainingPlanService trainingPlanService) {
        this.trainingPlanService = trainingPlanService;
    }

    @GetMapping("/plans")
    public ResponseEntity<List<TrainingPlanDTO>> getTrainingPlans() {

        return ResponseEntity.ok(trainingPlanService.getAllTrainingPlans());
    }
}