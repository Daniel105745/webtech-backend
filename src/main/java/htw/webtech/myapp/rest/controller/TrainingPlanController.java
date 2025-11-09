package htw.webtech.myapp.rest.controller;

import htw.webtech.myapp.business.service.TrainingPlanService;
import htw.webtech.myapp.rest.model.TrainingPlanDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/plans")
    public TrainingPlanDTO create(@RequestBody TrainingPlanDTO dto) {
        return trainingPlanService.create(dto);
    }


}