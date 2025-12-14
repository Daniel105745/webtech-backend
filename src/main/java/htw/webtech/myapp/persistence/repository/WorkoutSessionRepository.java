package htw.webtech.myapp.persistence.repository;

import htw.webtech.myapp.persistence.entity.WorkoutSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSessionEntity, Long> {

    // Suche Sessions für einen bestimmten User in einem Zeitraum
    List<WorkoutSessionEntity> findByUserIdAndStartTimeBetween(String userId, LocalDateTime start, LocalDateTime end);

    // Suche ALLE Sessions von einem User
    List<WorkoutSessionEntity> findAllByUserId(String userId);

    // (Optional) Laufende Sessions eines Users finden
    List<WorkoutSessionEntity> findByUserIdAndEndTimeIsNull(String userId);
}