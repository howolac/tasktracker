package com.tasktracker.security.jwt;

import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private final JWTUtils jwtUtils;
    private final JwtProperties jwtProperties;

    public JwtFilter(JWTUtils jwtUtils, JwtProperties jwtProperties) {
        this.jwtUtils = jwtUtils;
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = request.getHeader(jwtProperties.getHeader());
            if (token != null && token.startsWith(jwtProperties.getTokenPrefix())) {
                String plainToken = token.substring(jwtProperties.getTokenPrefix().length());
                if (jwtUtils.validateToken(plainToken)) {
                    String username = jwtUtils.getUsernameFromToken(plainToken);
                    List<SimpleGrantedAuthority> authorities = jwtUtils.getAuthoritiesFromToken(plainToken);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            logger.error("Unable to set user authentication in security context", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\": \"Invalid JWT token\", \"details\": \"" + e.getMessage() + "\"}");
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}