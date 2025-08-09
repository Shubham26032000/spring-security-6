package com.shubham.security.config;

import com.shubham.security.service.CustomUserDetails;
import com.shubham.security.service.CustomUserDetailsService;
import com.shubham.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAutheticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAutheticationFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        System.out.println("JwtToken : "+authHeader);//Token is in this header.
        if (authHeader == null || !authHeader.startsWith("Bearer")){   //Token null ignore.
            filterChain.doFilter(request,response);
        }

        final String jwtToken = authHeader.substring(7);
        final String userName = jwtService.extractUserName(jwtToken);
        System.out.println("JwtToken : "+jwtToken);
        System.out.println("userName : "+userName);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (userName != null && authentication == null){ //If not autheticated
            //Authenticate
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName); //load user from userdetails
            if (jwtService.isTokenValid(jwtToken,userDetails)){  //Match userdetails with token data.
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());  //This will be use for next filter.
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)); //Session ID for Token
                 SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request,response);
    }
}
