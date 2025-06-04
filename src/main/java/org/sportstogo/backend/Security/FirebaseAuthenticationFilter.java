package org.sportstogo.backend.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sportstogo.backend.Models.User;
import org.sportstogo.backend.Service.FirebaseTokenService;
import org.sportstogo.backend.Service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private final FirebaseTokenService firebaseTokenService;

    public FirebaseAuthenticationFilter(FirebaseTokenService firebaseTokenService) {
        this.firebaseTokenService = firebaseTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String idToken = authorizationHeader.substring(7); // Remove "Bearer "

            try {
                String uid = firebaseTokenService.verifyTokenAndGetUid(idToken);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(uid, null, Collections.emptyList());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // Token invalid
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Firebase token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
