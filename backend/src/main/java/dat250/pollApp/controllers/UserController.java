package dat250.pollApp.controllers;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dat250.pollApp.PollManager;
import dat250.pollApp.domain.User;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/users")
public class UserController {

    private final PollManager pm;

    public UserController(PollManager pm) {
        this.pm = pm;
    }

    // Create user
    @PostMapping
    public ResponseEntity<User> create(@RequestBody User body) {
        User saved = pm.addUser(body);  
        return ResponseEntity.ok(saved);
    }

    // List all users
    @GetMapping
    public Collection<User> list() {
        return pm.getAllUsers();
    }
    
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody Map<String, String> body) {
        User user = pm.findUserByUsername(body.get("username"));
        if (user == null) user = pm.findUserByEmail(body.get("email"));
        //if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }
}
