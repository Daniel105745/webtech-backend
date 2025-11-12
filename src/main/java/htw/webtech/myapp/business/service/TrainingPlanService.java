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
                        entity.getTrainingsziel()
                ))
                .toList();
    }

    public TrainingPlanDTO create(TrainingPlanDTO dto) {
        TrainingPlanEntity entity = new TrainingPlanEntity();
        entity.setName(dto.name());
        entity.setDauer(dto.dauer());
        entity.setIntensitaet(dto.intensitaet());
        entity.setTrainingsziel(dto.trainingsziel());

        // Speichern
        TrainingPlanEntity saved = repository.save(entity);

        // Und jetzt das DTO aus dem gespeicherten Entity zurückgeben
        return new TrainingPlanDTO(
                saved.getId().intValue(),
                saved.getName(),
                saved.getDauer(),
                saved.getIntensitaet(),
                saved.getTrainingsziel()
        );
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
    public TrainingPlanDTO update(Long id, TrainingPlanDTO dto) {
        return repository.findById(id)
                .map(entity -> {
                    entity.setName(dto.name());
                    entity.setDauer(dto.dauer());
                    entity.setIntensitaet(dto.intensitaet());
                    entity.setTrainingsziel(dto.trainingsziel());
                    repository.save(entity);
                    return new TrainingPlanDTO(
                            entity.getId().intValue(),
                            entity.getName(),
                            entity.getDauer(),
                            entity.getIntensitaet(),
                            entity.getTrainingsziel()
                    );
                })
                .orElse(null);
    }
    public TrainingPlanDTO getById(long id) {
        return repository.findById(id)
                .map(entity -> new TrainingPlanDTO(
                        entity.getId().intValue(),
                        entity.getName(),
                        entity.getDauer(),
                        entity.getIntensitaet(),
                        entity.getTrainingsziel()
                ))
                .orElse(null);
    }



}