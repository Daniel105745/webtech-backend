package htw.webtech.myapp.rest.controller;

import htw.webtech.myapp.business.service.TrainingPlanService;
import htw.webtech.myapp.rest.model.TrainingPlanDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://webtech-frontend-fe2m.onrender.com") // erlaubt NUR dein Frontend
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    public TrainingPlanController(TrainingPlanService trainingPlanService) {
        this.trainingPlanService = trainingPlanService;
    }

    @GetMapping("/plans")
    public List<TrainingPlanDTO> getAllPlans() {
        return trainingPlanService.getAllTrainingPlans();
    }

}