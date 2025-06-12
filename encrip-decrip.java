import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String CLIENT_ID = "YOUR_CLIENT_ID.apps.googleusercontent.com";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody Map<String, String> request) {
        String idTokenString = request.get("id_token");

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY)
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String userId = payload.getSubject(); // Use this value to identify the user
                String email = payload.getEmail();
                // Aquí puedes manejar la lógica de autenticación y crear una sesión

                return ResponseEntity.ok(Collections.singletonMap("success", true));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("success", false));
            }
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}
