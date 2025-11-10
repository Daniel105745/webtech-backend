package htw.webtech.myapp.business.service;

import htw.webtech.myapp.persistence.entity.TrainingPlanEntity;
import htw.webtech.myapp.persistence.entity.WorkoutEntity;
import htw.webtech.myapp.persistence.repository.TrainingPlanRepository;
import htw.webtech.myapp.persistence.repository.WorkoutRepository;
import htw.webtech.myapp.rest.model.WorkoutDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final TrainingPlanRepository trainingPlanRepository;

    public WorkoutService(WorkoutRepository workoutRepository, TrainingPlanRepository trainingPlanRepository) {
        this.workoutRepository = workoutRepository;
        this.trainingPlanRepository = trainingPlanRepository;
    }

    public List<WorkoutDTO> getAllByTrainingPlan(Long planId) {
        return workoutRepository.findByTrainingPlanId(planId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public WorkoutDTO getWorkoutById(Long id) {
        return workoutRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    public WorkoutDTO createWorkout(Long planId, WorkoutDTO dto) {
        TrainingPlanEntity plan = trainingPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("TrainingPlan nicht gefunden"));

        WorkoutEntity entity = new WorkoutEntity();
        entity.setName(dto.name());
        entity.setMuskelgruppe(dto.muskelgruppe());
        entity.setDayOfWeek(dto.dayOfWeek());
        entity.setTrainingPlan(plan);

        WorkoutEntity saved = workoutRepository.save(entity);
        return toDTO(saved);
    }

    public WorkoutDTO updateWorkout(Long id, WorkoutDTO dto) {
        Optional<WorkoutEntity> optionalWorkout = workoutRepository.findById(id);
        if (optionalWorkout.isEmpty()) return null;

        WorkoutEntity entity = optionalWorkout.get();
        entity.setName(dto.name());
        entity.setMuskelgruppe(dto.muskelgruppe());
        entity.setDayOfWeek(dto.dayOfWeek());

        WorkoutEntity updated = workoutRepository.save(entity);
        return toDTO(updated);
    }

    public boolean deleteWorkout(Long id) {
        if (!workoutRepository.existsById(id)) return false;
        workoutRepository.deleteById(id);
        return true;
    }

    private WorkoutDTO toDTO(WorkoutEntity entity) {
        return new WorkoutDTO(
                entity.getId(),
                entity.getName(),
                entity.getDayOfWeek(),
                entity.getMuskelgruppe(),
                entity.getTrainingPlan() != null ? entity.getTrainingPlan().getId() : null
        );
    }
}
