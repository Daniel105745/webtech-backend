package htw.webtech.myapp.controller;

import htw.webtech.myapp.model.TrainingPlan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class TrainingPlanController {

    @GetMapping("/plans")
    public List<TrainingPlan> getTrainingPlans() {
        return List.of(
                new TrainingPlan("Ganzkörper", "Muskelaufbau", 8),
                new TrainingPlan("Cardio", "Ausdauer verbessern", 6),
                new TrainingPlan("Oberkörper Split", "Kraftsteigerung", 10)
        );
    }
}
