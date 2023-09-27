package com.api.auth.security.filter;

import com.api.auth.service.JwtService;
import com.api.auth.service.UserDetailsCustomService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Getter
@RequiredArgsConstructor
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private JwtService jwtService;

    private UserDetailsCustomService userDetailsCustomService;

    @Autowired
    public SecurityFilter(JwtService jwtService, UserDetailsCustomService userDetailsCustomService) {
        this.jwtService = jwtService;
        this.userDetailsCustomService = userDetailsCustomService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String email = jwtService.validateToken(token);
        UserDetails userDetails = userDetailsCustomService.loadUserByUsername(email);

        var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authRequest = request.getHeader("Authorization");

        if (authRequest == null) {
            return null;
        }

        return authRequest.replace("Bearer ", "");
    }
}
