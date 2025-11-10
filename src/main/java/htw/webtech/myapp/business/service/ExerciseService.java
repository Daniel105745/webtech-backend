package htw.webtech.myapp.business.service;

import htw.webtech.myapp.persistence.entity.ExerciseEntity;
import htw.webtech.myapp.persistence.entity.WorkoutEntity;
import htw.webtech.myapp.persistence.repository.ExerciseRepository;
import htw.webtech.myapp.persistence.repository.WorkoutRepository;
import htw.webtech.myapp.rest.model.ExerciseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;

    public ExerciseService(ExerciseRepository exerciseRepository, WorkoutRepository workoutRepository) {
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
    }

    public List<ExerciseDTO> getExercisesByWorkout(Long workoutId) {
        return exerciseRepository.findByWorkoutId(workoutId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ExerciseDTO createExercise(ExerciseDTO dto, Long workoutId) {
        WorkoutEntity workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Workout nicht gefunden"));

        ExerciseEntity entity = new ExerciseEntity();
        entity.setName(dto.name());
        entity.setSaetze(dto.saetze());
        entity.setWiederholungen(dto.wiederholungen());
        entity.setGewicht(dto.gewicht());
        entity.setWorkout(workout);

        ExerciseEntity saved = exerciseRepository.save(entity);
        return toDTO(saved);
    }

    public ExerciseDTO updateExercise(Long id, ExerciseDTO dto) {
        Optional<ExerciseEntity> optional = exerciseRepository.findById(id);
        if (optional.isEmpty()) return null;

        ExerciseEntity existing = optional.get();
        existing.setName(dto.name());
        existing.setSaetze(dto.saetze());
        existing.setWiederholungen(dto.wiederholungen());
        existing.setGewicht(dto.gewicht());

        ExerciseEntity saved = exerciseRepository.save(existing);
        return toDTO(saved);
    }

    public boolean deleteExercise(Long id) {
        if (!exerciseRepository.existsById(id)) return false;
        exerciseRepository.deleteById(id);
        return true;
    }

    private ExerciseDTO toDTO(ExerciseEntity entity) {
        return new ExerciseDTO(
                entity.getId(),
                entity.getName(),
                entity.getSaetze(),
                entity.getWiederholungen(),
                entity.getGewicht(),
                entity.getWorkout() != null ? entity.getWorkout().getId() : null
        );
    }
}
