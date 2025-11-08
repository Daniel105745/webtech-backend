package htw.webtech.myapp.persistence.repository;

import htw.webtech.myapp.persistence.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestEntity, Long> {
}
