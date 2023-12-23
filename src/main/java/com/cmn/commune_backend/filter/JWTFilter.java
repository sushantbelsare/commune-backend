package com.cmn.commune_backend.filter;

import com.cmn.commune_backend.entity.User;
import com.cmn.commune_backend.repository.UserRepository;
import com.cmn.commune_backend.util.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
public class JWTFilter extends OncePerRequestFilter {

    JwtTokenUtil jwtTokenUtil;
    ObjectMapper mapper;

    UserRepository userRepository;

    JWTFilter(JwtTokenUtil jwtTokenUtil, UserRepository userRepository, ObjectMapper mapper){

        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (auth == null || !auth.contains("Bearer")){

            System.out.println(path);

            if (!path.equals("/v1/login") && !path.equals("/v1/register")){

                Map<String, Object> obj = new HashMap<>();

                obj.put("message", "unauthorized access");

                mapper.writeValue(createResponse(response).getWriter(), obj);
                return;
            }

            chain.doFilter(request, response);
            return;
        }

        final String token = request.getHeader(HttpHeaders.AUTHORIZATION).split(" ")[1].trim();

        if (!jwtTokenUtil.getUsernameFromToken(token).isEmpty()){
            Optional<User> user = userRepository.findById(jwtTokenUtil.getUsernameFromToken(token));
            if(user.isEmpty())
            {
                if (path.contains("login") || path.contains("register")){
                    chain.doFilter(request, response);
                    return;
                }

                Map<String, Object> obj = new HashMap<>();

                obj.put("message", "invalid user");

                mapper.writeValue(createResponse(response).getWriter(), obj);
                return;
            }
            else{
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user == null ? List.of() : getAuthorities("ROLE_USER")
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
                return;
            }

        }

        chain.doFilter(request, response);
    }

    private HttpServletResponse createResponse(HttpServletResponse response){

        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return response;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role){
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (role.equals("ROLE_USER")){
            authorities.add(new SimpleGrantedAuthority("READ_PRIVILEGE"));
        }
        return authorities;
    }

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
