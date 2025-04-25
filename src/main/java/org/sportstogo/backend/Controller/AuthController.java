package org.sportstogo.backend.Controller;

import org.sportstogo.backend.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController @RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/jwt")
    public ResponseEntity<?> getJwtFromFirebase(@RequestHeader("Authorization") String authHeader) {
        try {
            String firebaseToken = authHeader.replace("Bearer ", "");
            Map<String, String> jwtResponse = authService.authenticateWithFirebase(firebaseToken);
            return ResponseEntity.ok(jwtResponse);
        }
        catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid Firebase token");
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) throws Exception {
        String firebaseToken = body.get("token");

        if (firebaseToken == null || firebaseToken.isEmpty()) {
            return ResponseEntity.status(400).body("Token is required");
        }

        try {
            Map<String, String> response = authService.authenticateWithFirebase(firebaseToken);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid Firebase token");
        }
    }
}