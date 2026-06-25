package oil_cost_backend.repository;

import oil_cost_backend.entity.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractorRepository extends JpaRepository<Contractor, String> {
}
