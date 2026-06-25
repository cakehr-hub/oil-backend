package oil_cost_backend.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "material_code")
public class MaterialCode {

    @Id
    @Column(name = "material_code")
    private String materialCode;

    @Column(name = "material_name")
    private String materialName;

    @Column(name = "material_unit")
    private String materialUnit;
}
