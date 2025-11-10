package htw.webtech.myapp.persistence.repository;

import htw.webtech.myapp.persistence.entity.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<ExerciseEntity, Long> {
    List<ExerciseEntity> findByWorkoutId(Long workoutId);
}
