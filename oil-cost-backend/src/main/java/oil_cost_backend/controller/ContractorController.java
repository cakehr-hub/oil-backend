package oil_cost_backend.controller;

import oil_cost_backend.entity.Contractor;
import oil_cost_backend.repository.ContractorRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contractors")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ContractorController {

    private final ContractorRepository contractorRepository;

    public ContractorController(ContractorRepository contractorRepository) {
        this.contractorRepository = contractorRepository;
    }

    // 查询全部
    @GetMapping
    public List<Contractor> list() {
        return contractorRepository.findAll();
    }

    // 新增
    @PostMapping
    public Contractor add(@RequestBody Contractor contractor) {
        return contractorRepository.save(contractor);
    }

    // 修改（按原名称传参，可改名称）
    @PutMapping("/{oldName}")
    public Contractor update(@PathVariable String oldName, @RequestBody Contractor contractor) {
        // 如果改了名称，先删除旧记录再保存新记录
        contractorRepository.deleteById(oldName);
        return contractorRepository.save(contractor);
    }

    // 删除
    @DeleteMapping("/{contractorName}")
    public String delete(@PathVariable String contractorName) {
        contractorRepository.deleteById(contractorName);
        return "删除成功";
    }
}
