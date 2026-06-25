package oil_cost_backend.controller;

import oil_cost_backend.entity.SysUser;
import oil_cost_backend.repository.SysUserRepository;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    private final SysUserRepository sysUserRepository;

    public AuthController(SysUserRepository sysUserRepository) {
        this.sysUserRepository = sysUserRepository;
    }

    // 登录接口
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData, HttpSession session) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        Map<String, Object> result = new HashMap<>();

        SysUser user = sysUserRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // 登录成功，将用户信息存入 session
            session.setAttribute("user", user);
            result.put("success", true);
            result.put("message", "登录成功");
            result.put("role", user.getRoleName());
            result.put("username", user.getUsername());
        } else {
            result.put("success", false);
            result.put("message", "用户名或密码错误");
        }
        return result;
    }

    // 获取当前登录用户信息（用于前端判断是否已登录）
    @GetMapping("/current")
    public Map<String, Object> currentUser(HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        if (user != null) {
            result.put("authenticated", true);
            result.put("username", user.getUsername());
            result.put("role", user.getRoleName());
        } else {
            result.put("authenticated", false);
        }
        return result;
    }

    // 退出登录
    @PostMapping("/logout")
    public Map<String, String> logout(HttpSession session) {
        session.removeAttribute("user");
        Map<String, String> result = new HashMap<>();
        result.put("message", "已退出登录");
        return result;
    }
}
