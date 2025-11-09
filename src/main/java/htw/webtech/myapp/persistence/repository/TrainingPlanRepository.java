package htw.webtech.myapp.persistence.repository;

import htw.webtech.myapp.persistence.entity.TrainingPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingPlanRepository extends JpaRepository<TrainingPlanEntity, Long> {
}