package htw.webtech.myapp.persistence.repository;

import htw.webtech.myapp.persistence.entity.WorkoutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<WorkoutEntity, Long> {
    List<WorkoutEntity> findByTrainingPlanId(Long trainingPlanId);
}
