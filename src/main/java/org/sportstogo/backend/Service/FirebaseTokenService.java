package org.sportstogo.backend.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

@Service
public class FirebaseTokenService {

    public String verifyTokenAndGetUid(String idToken) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            return decodedToken.getUid();
        } catch (Exception e) {
            throw new RuntimeException("Invalid Firebase ID token: " + e.getMessage());
        }
    }

    public static String getDisplayNameFromUid(String uid) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
            return userRecord.getDisplayName();
        } catch (FirebaseAuthException e) {
            throw new RuntimeException("Failed to retrieve display name from Firebase ID: " + e.getMessage());
        }
    }
}
