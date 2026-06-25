package oil_cost_backend.controller;

import oil_cost_backend.entity.WorkProject;
import oil_cost_backend.repository.WorkProjectRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class WorkProjectController {

    private final WorkProjectRepository workProjectRepository;

    public WorkProjectController(WorkProjectRepository workProjectRepository) {
        this.workProjectRepository = workProjectRepository;
    }

    @GetMapping
    public List<WorkProject> list() {
        return workProjectRepository.findAll();
    }

    @GetMapping("/{projectNo}")
    public WorkProject getByProjectNo(@PathVariable String projectNo) {
        return workProjectRepository.findById(projectNo).orElse(null);
    }

    @PostMapping
    public WorkProject add(@RequestBody WorkProject workProject) {
        return workProjectRepository.save(workProject);
    }

    @PutMapping("/{projectNo}")
    public WorkProject update(@PathVariable String projectNo, @RequestBody WorkProject workProject) {
        workProject.setProjectNo(projectNo);
        return workProjectRepository.save(workProject);
    }

    @DeleteMapping("/{projectNo}")
    public String delete(@PathVariable String projectNo) {
        workProjectRepository.deleteById(projectNo);
        return "删除成功";
    }
}
