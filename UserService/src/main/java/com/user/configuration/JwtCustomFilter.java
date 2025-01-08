package com.user.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtCustomFilter extends OncePerRequestFilter {
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    JwtService jwtService;

    private final String bearer="Bearer ";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        System.out.println("Current path: " + path);
        return path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui.html") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/webjars") ||
                path.startsWith("/auth/login") ||
                path.startsWith("/auth/register");
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader=request.getHeader("Authorization");
        System.out.println("jwt filter running");
            String email=null;
            String token=null;

            if(authorizationHeader !=null && authorizationHeader.startsWith(bearer)){
                token=authorizationHeader.substring(bearer.length());
                email=jwtService.extractUsername(token);
            }
            if(email !=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails= customUserDetailsService.loadUserByUsername(email);
                if(jwtService.validateToken(token,userDetails)){
                    System.out.println("Validated");
                    UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                            userDetails,null,userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request,response);
    }
}
