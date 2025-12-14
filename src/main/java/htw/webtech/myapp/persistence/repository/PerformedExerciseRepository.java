package htw.webtech.myapp.persistence.repository;

import htw.webtech.myapp.persistence.entity.PerformedExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerformedExerciseRepository extends JpaRepository<PerformedExerciseEntity, Long> {
    List<PerformedExerciseEntity> findBySessionId(Long sessionId);
}