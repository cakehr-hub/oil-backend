package oil_cost_backend.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "work_project")
public class WorkProject {

    @Id
    @Column(name = "project_no")
    private String projectNo;

    @Column(name = "budget_unit")
    private String budgetUnit;

    @Column(name = "well_no")
    private String wellNo;

    @Column(name = "budget_amount")
    private BigDecimal budgetAmount;

    @Column(name = "budget_person")
    private String budgetPerson;

    @Column(name = "budget_date")
    private LocalDate budgetDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "finish_date")
    private LocalDate finishDate;

    @Column(name = "contractor_name")
    private String contractorName;

    @Column(name = "work_content")
    private String workContent;

    @Column(name = "material_fee")
    private BigDecimal materialFee;

    @Column(name = "labor_fee")
    private BigDecimal laborFee;

    @Column(name = "equipment_fee")
    private BigDecimal equipmentFee;

    @Column(name = "other_fee")
    private BigDecimal otherFee;

    @Column(name = "settlement_amount")
    private BigDecimal settlementAmount;

    @Column(name = "settlement_person")
    private String settlementPerson;

    @Column(name = "settlement_date")
    private LocalDate settlementDate;

    @Column(name = "account_amount")
    private BigDecimal accountAmount;

    @Column(name = "account_person")
    private String accountPerson;

    @Column(name = "account_date")
    private LocalDate accountDate;
}
