package htw.webtech.myapp.persistence.repository;

import htw.webtech.myapp.persistence.entity.TrainingPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TrainingPlanRepository extends JpaRepository<TrainingPlanEntity, Long> {
    List<TrainingPlanEntity> findAllByUserId(String userId);
}