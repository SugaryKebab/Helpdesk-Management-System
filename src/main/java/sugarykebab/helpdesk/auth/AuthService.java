package sugarykebab.helpdesk.auth;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sugarykebab.helpdesk.config.JwtService;
import sugarykebab.helpdesk.dto.ChangePasswordDto;
import sugarykebab.helpdesk.dto.ResetPasswordDto;
import sugarykebab.helpdesk.entities.AppUser;
import sugarykebab.helpdesk.entities.AppUserDetail;
import sugarykebab.helpdesk.entities.Organization;
import sugarykebab.helpdesk.entities.PasswordResetToken;
import sugarykebab.helpdesk.repositories.AppUserRepository;
import sugarykebab.helpdesk.repositories.PasswordResetTokenRepository;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EntityManager entityManager;
    private final PasswordResetTokenRepository tokenRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        Organization orgRef = entityManager.getReference(Organization.class, request.getOrgID());

        var user = AppUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .org(orgRef)
                .role(request.getRole())
                .build();



        appUserRepository.save(user);
        var jwtToken= jwtService.generateToken(new AppUserDetail(user));
        return AuthenticationResponse.builder().token(jwtToken).build();

    }

    public AuthenticationResponse authenticate(AuthRequest request) {
        System.out.println("working");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

        } catch (Exception e) {
            System.out.println("Authentication failed: " + e.getClass().getSimpleName()
                    + " - " + e.getMessage());
            throw e;
        }

        var optionalUser = appUserRepository.findByEmail(request.getEmail());

        var user = optionalUser.orElseThrow(() ->
                new UsernameNotFoundException("User found via AuthenticationManager but not in repository.")
        );

        var jwtToken= jwtService.generateToken(new AppUserDetail(user));
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public String forgotPassword(String email) {
        var user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No user found with email: " + email));


        tokenRepository.invalidateExistingTokensForUser(user.getId());

        String token = UUID.randomUUID().toString();
        PasswordResetToken prt = new PasswordResetToken();
        prt.setUser(user);
        prt.setToken(token);
        prt.setExpiresAt(Instant.now().plusSeconds(15 * 60)); // 15 min expiry
        tokenRepository.save(prt);


        tokenRepository.deleteExpiredOrUsedTokens(Instant.now());

        return token;
    }

    public void resetPassword(ResetPasswordDto dto) {
        PasswordResetToken prt = tokenRepository.findByToken(dto.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (prt.isUsed() || prt.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Token expired or already used");
        }

        AppUser user = prt.getUser();
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        appUserRepository.save(user);

        prt.setUsed(true);
        tokenRepository.save(prt);


        tokenRepository.deleteExpiredOrUsedTokens(Instant.now());
    }

    public void changePassword(AppUser user, ChangePasswordDto dto) {
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        appUserRepository.save(user);
    }

    public AuthenticationResponse registerSuperAdmin(RegisterRequest request) {
        Organization orgRef = entityManager.getReference(Organization.class, request.getOrgID());

        var user = AppUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .org(orgRef)
                .role(request.getRole())
                .build();

        appUserRepository.save(user);
        var jwtToken = jwtService.generateToken(new AppUserDetail(user));
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse registerAdmin(RegisterRequest request, AppUser admin) {
        System.out.println("hello");
        if (admin.getOrg() == null) {
            throw new RuntimeException("Admin has no organization assigned");
        }
        System.out.println("hello1");
        var user = AppUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .org(admin.getOrg()) // force same org
                .role(request.getRole())
                .build();

        appUserRepository.save(user);
        var jwtToken = jwtService.generateToken(new AppUserDetail(user));
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
