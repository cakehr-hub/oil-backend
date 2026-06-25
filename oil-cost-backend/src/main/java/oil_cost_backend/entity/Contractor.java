package oil_cost_backend.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "contractor")
public class Contractor {

    @Id
    @Column(name = "contractor_name")
    private String contractorName;
}
