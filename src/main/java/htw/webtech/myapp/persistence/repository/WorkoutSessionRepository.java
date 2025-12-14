package htw.webtech.myapp.persistence.repository;

import htw.webtech.myapp.persistence.entity.WorkoutSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSessionEntity, Long> {

    List<WorkoutSessionEntity> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    List<WorkoutSessionEntity> findByEndTimeIsNull();
}
