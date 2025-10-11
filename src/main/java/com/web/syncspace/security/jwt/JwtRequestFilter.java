package com.web.syncspace.security.jwt;

import com.web.syncspace.config.AuthEntryPoint;
import com.web.syncspace.exceptions.tokenexceptions.*;
import com.web.syncspace.security.userdetaiils.CustomUserDetails;
import com.web.syncspace.security.userdetaiils.CustomUserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsServiceImpl customUserDetailsService;
    private final JwtBlacklistService jwtBlacklistService;
    private final Pattern jwtPattern = Pattern.compile("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+$");
    private final AuthEntryPoint authEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws IOException {

        final String authHeader = request.getHeader("Authorization");
        String tokenPrefix = "Bearer ";
        try {
            if (authHeader == null || !authHeader.startsWith(tokenPrefix)) {
                filterChain.doFilter(request, response);
                return;
            }

            final String token = authHeader.substring(tokenPrefix.length());

            if (!token.matches(jwtPattern.pattern())) {
                authEntryPoint.commence(request, response, new JwtTokenException("Invalid or malformed token!!!"));
                return;
            }

            if (!jwtUtil.isTokenStructureValid(token)) {
                authEntryPoint.commence(request, response, new MalformedTokenException("Invalid or malformed token!!!"));
                return;
            }

            String username;
            try {
                username = jwtUtil.extractUsername(token);
            } catch (Exception e) {
                authEntryPoint.commence(request, response, new JwtTokenException("Invalid token!!!"));
                return;
            }

            if (jwtBlacklistService.isTokenBlacklisted(token)) {
                authEntryPoint.commence(request, response, new TokenBlacklistedException("Token is blacklisted!!!"));
                return;
            }

            if (jwtUtil.isTokenExpired(token)) {
                authEntryPoint.commence(request, response, new TokenExpiredException("Token is expired!!!"));
                return;
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
                if (jwtUtil.isTokenValid(token, customUserDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    authEntryPoint.commence(request, response, new JwtTokenException("Token is not valid!!!"));
                    return;
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            authEntryPoint.commence(request, response, new InvalidTokenException(e.getMessage()));
        }
    }
}
