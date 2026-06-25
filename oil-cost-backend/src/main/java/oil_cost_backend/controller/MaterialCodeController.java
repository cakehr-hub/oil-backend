package oil_cost_backend.controller;

import oil_cost_backend.entity.MaterialCode;
import oil_cost_backend.repository.MaterialCodeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class MaterialCodeController {

    private final MaterialCodeRepository materialCodeRepository;

    public MaterialCodeController(MaterialCodeRepository materialCodeRepository) {
        this.materialCodeRepository = materialCodeRepository;
    }

    // 查询全部
    @GetMapping
    public List<MaterialCode> list() {
        return materialCodeRepository.findAll();
    }

    // 新增
    @PostMapping
    public MaterialCode add(@RequestBody MaterialCode materialCode) {
        return materialCodeRepository.save(materialCode);
    }

    // 修改
    @PutMapping("/{oldCode}")
    public MaterialCode update(@PathVariable String oldCode, @RequestBody MaterialCode materialCode) {
        materialCodeRepository.deleteById(oldCode);
        return materialCodeRepository.save(materialCode);
    }

    // 删除
    @DeleteMapping("/{materialCode}")
    public String delete(@PathVariable String materialCode) {
        materialCodeRepository.deleteById(materialCode);
        return "删除成功";
    }
}
