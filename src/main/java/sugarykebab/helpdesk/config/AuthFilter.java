package sugarykebab.helpdesk.config;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String usermail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        usermail = jwtService.getUsername(jwt);


        System.out.println("Auth header: " + authHeader);
        System.out.println("JWT: " + jwt);
        System.out.println("Username from JWT: " + usermail);

        UserDetails userDetails = userDetailsService.loadUserByUsername(usermail);
        System.out.println("UserDetails loaded: " + userDetails.getUsername());
        System.out.println("Authorities from UserDetails: " + userDetails.getAuthorities());

        boolean valid = jwtService.validateToken(jwt, userDetails);
        System.out.println("Token valid: " + valid);


        if (usermail != null && SecurityContextHolder.getContext().getAuthentication() == null) {



            if (jwtService.validateToken(jwt, userDetails)) {


                String role = jwtService.getClaim(jwt, claims -> claims.get("role", String.class));
                System.out.println(role);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                 userDetails.getAuthorities()
                        );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                System.out.println("Authorities in SecurityContext: " +
                        SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            }
        }


        filterChain.doFilter(request, response);


    }
}
