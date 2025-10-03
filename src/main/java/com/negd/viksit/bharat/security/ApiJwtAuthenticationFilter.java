package com.negd.viksit.bharat.security;

import com.negd.viksit.bharat.exception.UnauthorizedException;
import com.negd.viksit.bharat.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiJwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsService userDetailsService;


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain chain) throws ServletException, IOException {

        String requestHeader = request.getHeader("Authorization");
        try {
            if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
                String authToken = requestHeader.substring(7);
                String username = jwtTokenUtil.getUsernameFromToken(authToken);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User userEntity = (User) userDetailsService.loadUserByUsername(username);

                    if (userEntity.isLoggedOut()) {
                        throw new UnauthorizedException("User is logged out");
                    }

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userEntity, null, userEntity.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            log.error("Error during user authorization:{}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            response.getWriter()
                    .write(Objects.requireNonNull(
                            com.negd.viksit.bharat.util.HelperUtil.toJson(new ResponseEntity("Unauthorized access", HttpStatus.UNAUTHORIZED))));
            return;
        }

        chain.doFilter(request, response);
    }

}
