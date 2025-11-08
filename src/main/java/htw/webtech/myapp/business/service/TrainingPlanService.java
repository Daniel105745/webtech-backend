package htw.webtech.myapp.business.service;

import htw.webtech.myapp.rest.model.TrainingPlanDTO;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class TrainingPlanService {

    public List<TrainingPlanDTO> getAllTrainingPlans() {
        return List.of(
                new TrainingPlanDTO(1, "Anfänger Plan", "4 Wochen", "Leicht", "Ganzkörper"),
                new TrainingPlanDTO(2, "Muskelaufbau Plan", "8 Wochen", "Mittel", "Oberkörper, Arme"),
                new TrainingPlanDTO(3, "Fettverbrennung Plan", "6 Wochen", "Hoch", "Beine, Core")
        );
    }
}
