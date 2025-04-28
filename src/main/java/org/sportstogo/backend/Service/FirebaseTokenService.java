package org.sportstogo.backend.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

@Service
public class FirebaseTokenService {

    public String verifyTokenAndGetUid(String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            System.out.println("Decoded token UID: " + decodedToken.getUid());
            return decodedToken.getUid();
        } catch (Exception e) {
            throw new RuntimeException("Invalid Firebase ID token");
        }
    }
}
