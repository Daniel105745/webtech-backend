package htw.webtech.myapp.business.service;

import htw.webtech.myapp.persistence.entity.TrainingPlanEntity;
import htw.webtech.myapp.persistence.repository.TrainingPlanRepository;
import htw.webtech.myapp.rest.model.TrainingPlanDTO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TrainingPlanService {

    private final TrainingPlanRepository repository;

    public TrainingPlanService(TrainingPlanRepository repository) {
        this.repository = repository;
    }

    public List<TrainingPlanDTO> getAllTrainingPlans() {
        return repository.findAll().stream()
                .map(entity -> new TrainingPlanDTO(
                        entity.getId().intValue(),
                        entity.getName(),
                        entity.getDauer(),
                        entity.getIntensitaet(),
                        entity.getZielmuskeln()
                ))
                .toList();
    }

    public TrainingPlanDTO create(TrainingPlanDTO dto) {
        TrainingPlanEntity entity = new TrainingPlanEntity();
        entity.setName(dto.name());
        entity.setDauer(dto.dauer());
        entity.setIntensitaet(dto.intensitaet());
        entity.setZielmuskeln(dto.zielmuskeln());
        repository.save(entity);
        return dto;
    }
}