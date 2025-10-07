package sugarykebab.helpdesk.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sugarykebab.helpdesk.dto.ChangePasswordDto;
import sugarykebab.helpdesk.dto.ResetPasswordDto;
import sugarykebab.helpdesk.entities.AppUserDetail;
import sugarykebab.helpdesk.utils.ResponseHelper;

import java.util.Map;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            var response = authService.register(request);
            return ResponseHelper.respondCreated(response, "Failed to register user");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest request,
                                           @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            var response = authService.registerAdmin(request, currentUser.getAppUser());
            return ResponseHelper.respondCreated(response, "Failed to create user");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @PostMapping("/register/superadmin")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<?> registerSuperAdmin(@RequestBody RegisterRequest request) {
        try {
            var response = authService.registerSuperAdmin(request);
            return ResponseHelper.respondCreated(response, "Failed to create user");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest request) {
        try {
            var response = authService.authenticate(request);
            return ResponseHelper.respondSingle(response, "Authentication failed");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> req) {
        try {
            var token = authService.forgotPassword(req.get("email"));
            Map<String, String> resp = Map.of(
                    "email", req.get("email"),
                    "token", token
            );
            return ResponseHelper.respondCreated(resp, "Failed to generate reset token");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto dto) {
        try {
            authService.resetPassword(dto);
            return ResponseHelper.respondCreated("Password has been reset", "Failed to reset password");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto dto,
                                            @AuthenticationPrincipal AppUserDetail currentUser) {
        try {
            authService.changePassword(currentUser.getAppUser(), dto);
            return ResponseHelper.respondCreated("Password changed successfully", "Failed to change password");
        } catch (RuntimeException e) {
            return ResponseHelper.respondError(e.getMessage());
        }
    }
}
