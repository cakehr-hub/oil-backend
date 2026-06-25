package oil_cost_backend.controller;

import oil_cost_backend.repository.WorkProjectRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class StatisticsController {

    private final WorkProjectRepository workProjectRepository;

    public StatisticsController(WorkProjectRepository workProjectRepository) {
        this.workProjectRepository = workProjectRepository;
    }

    // 各承包商费用统计（预算金额、结算金额总和）
    @GetMapping("/contractor-fee")
    public List<Map<String, Object>> contractorFeeStats() {
        return workProjectRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        p -> p.getContractorName() != null ? p.getContractorName() : "未知",
                        Collectors.reducing(
                                new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO},
                                p -> new BigDecimal[]{
                                        p.getBudgetAmount() != null ? p.getBudgetAmount() : BigDecimal.ZERO,
                                        p.getSettlementAmount() != null ? p.getSettlementAmount() : BigDecimal.ZERO
                                },
                                (a, b) -> new BigDecimal[]{a[0].add(b[0]), a[1].add(b[1])}
                        )
                ))
                .entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("contractorName", entry.getKey());
                    map.put("budgetTotal", entry.getValue()[0]);
                    map.put("settlementTotal", entry.getValue()[1]);
                    return map;
                })
                .collect(Collectors.toList());
    }

    // 月度作业数量统计（按开始日期月份分组）
    @GetMapping("/monthly-count")
    public List<Map<String, Object>> monthlyCountStats() {
        return workProjectRepository.findAll().stream()
                .filter(p -> p.getStartDate() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getStartDate().getYear() + "-" + String.format("%02d", p.getStartDate().getMonthValue()),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("month", entry.getKey());
                    map.put("count", entry.getValue());
                    return map;
                })
                .sorted(Comparator.comparing(m -> m.get("month").toString()))
                .collect(Collectors.toList());
    }
}
