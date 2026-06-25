package oil_cost_backend.repository;

import oil_cost_backend.entity.WorkProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkProjectRepository extends JpaRepository<WorkProject, String> {
}
