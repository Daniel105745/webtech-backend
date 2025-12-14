package htw.webtech.myapp.business.service;

import htw.webtech.myapp.persistence.entity.TrainingPlanEntity;
import htw.webtech.myapp.persistence.entity.WorkoutEntity;
import htw.webtech.myapp.persistence.entity.WorkoutSessionEntity;
import htw.webtech.myapp.persistence.repository.TrainingPlanRepository;
import htw.webtech.myapp.persistence.repository.WorkoutSessionRepository;
import htw.webtech.myapp.rest.model.TrainingPlanDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingPlanService {

    private final TrainingPlanRepository repository;
    // NEU: Damit wir die Sessions aufräumen können
    private final WorkoutSessionRepository sessionRepository;

    public TrainingPlanService(TrainingPlanRepository repository,
                               WorkoutSessionRepository sessionRepository) {
        this.repository = repository;
        this.sessionRepository = sessionRepository;
    }

    public List<TrainingPlanDTO> getAllTrainingPlans(String userId) {
        return repository.findAllByUserId(userId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public TrainingPlanDTO create(TrainingPlanDTO dto, String userId) {
        TrainingPlanEntity entity = new TrainingPlanEntity();
        entity.setName(dto.name());
        entity.setDauer(dto.dauer());
        entity.setIntensitaet(dto.intensitaet());
        entity.setTrainingsziel(dto.trainingsziel());
        entity.setUserId(userId);

        TrainingPlanEntity saved = repository.save(entity);
        return toDTO(saved);
    }

    // --- HIER IST DER FIX FÜR TRAINING PLAN DELETE ---
    public boolean delete(Long id) {
        Optional<TrainingPlanEntity> planOpt = repository.findById(id);
        if (planOpt.isEmpty()) {
            return false;
        }

        TrainingPlanEntity plan = planOpt.get();

        // 1. Wir holen ALLE Sessions (einfachste Lösung für das Projekt)
        List<WorkoutSessionEntity> allSessions = sessionRepository.findAll();

        // 2. Wir gehen alle Workouts dieses Plans durch
        for (WorkoutEntity workout : plan.getWorkouts()) {
            // Und schauen, ob irgendeine Session dieses Workout benutzt
            for (WorkoutSessionEntity session : allSessions) {
                if (session.getWorkout() != null &&
                        session.getWorkout().getId().equals(workout.getId())) {

                    // Verbindung lösen!
                    session.setWorkout(null);
                    sessionRepository.save(session);
                }
            }
        }

        // 3. Jetzt können wir den Plan löschen (löscht Workouts via Cascade mit)
        repository.deleteById(id);
        return true;
    }

    public TrainingPlanDTO update(Long id, TrainingPlanDTO dto) {
        return repository.findById(id)
                .map(entity -> {
                    entity.setName(dto.name());
                    entity.setDauer(dto.dauer());
                    entity.setIntensitaet(dto.intensitaet());
                    entity.setTrainingsziel(dto.trainingsziel());
                    TrainingPlanEntity saved = repository.save(entity);
                    return toDTO(saved);
                })
                .orElse(null);
    }

    public TrainingPlanDTO getById(long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    private TrainingPlanDTO toDTO(TrainingPlanEntity entity) {
        return new TrainingPlanDTO(
                entity.getId().intValue(),
                entity.getName(),
                entity.getDauer(),
                entity.getIntensitaet(),
                entity.getTrainingsziel()
        );
    }
}