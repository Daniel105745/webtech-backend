package htw.webtech.myapp.business.service;

import htw.webtech.myapp.rest.model.TrainingPlanDTO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TrainingPlanService {

    public List<TrainingPlanDTO> getAllTrainingPlans() {
        return List.of(
                new TrainingPlanDTO("Anfänger Plan", "Grundfitness aufbauen", 8),
                new TrainingPlanDTO("Muskelaufbau Plan", "Masse aufbauen", 12),
                new TrainingPlanDTO("Ausdauer Plan", "Kondition verbessern", 10)
        );
    }
}
