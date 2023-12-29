package com.dms.backend.usersapp.backendusersapp.auth.filters;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
//import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import static com.dms.backend.usersapp.backendusersapp.auth.TokenJwtConfig.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.dms.backend.usersapp.backendusersapp.models.entities.User;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        
        User user = null;
        String username = null;   
        String password = null;     

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password = user.getPassword();

        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        String username = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername();       
                                   //token generico


        Collection<?extends GrantedAuthority> roles = authResult.getAuthorities();   
        boolean isAdmin = roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        Claims claims = Jwts.claims().build();
     //   claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
     //   claims.put("isAdmin", isAdmin);      

        String token = Jwts.builder()
                           //.claims().empty().add(claims).and()
                           .subject(username)
                           .claim("authorities", new ObjectMapper().writeValueAsString(roles))
                           .claim("isAdmin", isAdmin)
                           .claims(claims)
                           .signWith(key)
                           .issuedAt(new Date())
                           .expiration(new Date(System.currentTimeMillis() + 3600000 ))
                           .compact();   //token encriptado     

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);             //cabecera con token en Bearer

        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("username", username);
        body.put("message", String.format("Hi, " + username + " session success!!"));

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));  //lo pasamos a json
        response.setStatus(200);
        response.setContentType("application/json");   
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
       
        Map<String, Object> body = new HashMap<>();        
        body.put("message", String.format("Error in auth!!"));
        body.put("error", failed.getMessage());
        
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));  //lo pasamos a json
        response.setStatus(401);
        response.setContentType("application/json"); 
    }
    
}

