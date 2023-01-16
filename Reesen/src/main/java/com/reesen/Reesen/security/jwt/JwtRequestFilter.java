package com.reesen.Reesen.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        if(request.getRequestURL().toString().contains("/api")){
            String requestTokenHeader = request.getHeader("X-Auth-Token");
            String refreshToken = request.getHeader("refreshToken");
            String jwtToken = null;
            if(requestTokenHeader != null){
                try {
                    jwtToken = requestTokenHeader;
                    System.out.println(jwtToken);
                    authenticateToken(request, jwtToken);
                }catch (IllegalArgumentException e) {
                    System.out.println("Unable to get JWT Token.");
                } catch (ExpiredJwtException e) {
                    try {
                        authenticateToken(request, refreshToken);
                    }catch (ExpiredJwtException j){
                        System.out.println("Refresh token has expired.");
                    }
                    System.out.println("JWT Token has expired.");
                } catch (io.jsonwebtoken.MalformedJwtException e) {
                    System.out.println("Bad JWT Token.");
                }
            }else{
                if(refreshToken != null){
                    allowForRefreshToken(request, refreshToken);
                }else{
                    logger.warn("INVALID JWT TOKEN");
                }
            }
        }
        filterChain.doFilter(request, response);

    }

    private void authenticateToken(HttpServletRequest request, String jwtToken) {
        String username;
        username = jwtTokenUtil.getUsername(jwtToken);
        UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }

    private void allowForRefreshToken(HttpServletRequest request, String refreshToken) {
       authenticateToken(request, refreshToken);
    }
}
