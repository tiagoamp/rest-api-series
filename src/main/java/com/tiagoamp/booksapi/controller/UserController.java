package com.tiagoamp.booksapi.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiagoamp.booksapi.model.AppUser;
import com.tiagoamp.booksapi.model.Role;
import com.tiagoamp.booksapi.service.TokenService;
import com.tiagoamp.booksapi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;


    @GetMapping
    public ResponseEntity<List<AppUser>> getUsers() {
        List<AppUser> users = userService.find();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser user) {
        user = userService.save(user);
        return ResponseEntity.created(URI.create(user.getId().toString())).build();
    }

    @PostMapping("role")
    public ResponseEntity<AppUser> createRole(@RequestBody Role role) {
        role = userService.save(role);
        return ResponseEntity.created(URI.create(role.getId().toString())).build();
    }

    @PostMapping("{username}/role/{rolename}")
    public ResponseEntity<?> addRoleToUser(@PathVariable("username") String username, @PathVariable("rolename") String roleName) {
        AppUser user = userService.addRoleToUser(username, roleName);
        return ResponseEntity.ok(user);
    }

    @GetMapping("refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            String refreshToken = tokenService.getTokenFrom(authorizationHeader);
            DecodedJWT decodedJWT = tokenService.getDecodedTokenFrom(refreshToken);
            String username = decodedJWT.getSubject();
            AppUser user = userService.find(username);
            String accessToken = tokenService.generateAccessToken(user);
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", accessToken);
            tokens.put("refresh_token", refreshToken);  // reuse refresh token until it expires
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        } catch (Exception exception) {
            exception.printStackTrace();  // log error
            response.setHeader("error", exception.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            Map<String, String> error = new HashMap<>();
            error.put("error_msg", exception.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }

}
