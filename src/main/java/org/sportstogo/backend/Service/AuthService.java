package org.sportstogo.backend.Service;

import org.sportstogo.backend.Security.JwtUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class AuthService {
    private final JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public Map<String, String> authenticateWithFirebase(String firebaseIdToken) throws Exception {
        if (firebaseIdToken == null || firebaseIdToken.trim().isEmpty()) {
            throw new IllegalArgumentException("ID token must not be null or empty");
        }

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseIdToken);
            String uid = decodedToken.getUid();
            String role = "USER";

            if (decodedToken.getEmail().equals("admin@example.com")) {
                role = "ADMIN";
            }

            String jwt = jwtUtil.generateToken(uid, role);

            return Map.of("token", jwt, "role", role);
        } catch (Exception e) {
            throw new Exception("Firebase authentication failed: " + e.getMessage());
        }
    }
}
