package oil_cost_backend.repository;

import oil_cost_backend.entity.MaterialCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialCodeRepository extends JpaRepository<MaterialCode, String> {
}
