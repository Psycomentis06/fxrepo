package com.github.psycomentis06.fxrepomain.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.psycomentis06.fxrepomain.entity.Role;
import com.github.psycomentis06.fxrepomain.entity.User;
import com.github.psycomentis06.fxrepomain.model.*;
import com.github.psycomentis06.fxrepomain.repository.RoleRepository;
import com.github.psycomentis06.fxrepomain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.type.descriptor.java.ObjectJavaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/")
public class SecurityController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Value("${app.auth.token.key}")
    private String tokenKey;

    @Value("${app.auth.refresh-token.key}")
    private String refreshTokenKey;

    public SecurityController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @PostMapping("login")
    public ResponseEntity<Object> authenticate(
            @RequestBody LoginModel login,
            HttpServletRequest request,
            @RequestHeader(value = "User-Agent") String userAgent
    ) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword())
            );
        } catch (BadCredentialsException e) {
            var res = new ResponseModel();
            res
                    .setMessage("Invalid Credentials")
                    .setStatus(HttpStatus.UNAUTHORIZED)
                    .setCode(HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(res, res.getStatus());
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(login.getUsername());
        var date = Instant.now();
        var token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(date)
                .withExpiresAt(date.plusSeconds(60))
                .withIssuer("fx-repo@" + request.getRemoteHost())
                .withClaim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .sign(Algorithm.HMAC256(tokenKey));
        var refreshToken = JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(date)
                .withExpiresAt(date.plusSeconds(86400))
                .withIssuer("fx-repo@" + request.getRemoteHost())
                .withClaim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withClaim("address", request.getRemoteUser())
                .withClaim("agent", userAgent)
                .sign(Algorithm.HMAC256(refreshTokenKey));

        HashMap<String, String> resHash = new HashMap<>(2);
        resHash.put("token", token);
        resHash.put("refreshToken", refreshToken);
        var res = new ResponseObjModel();
        res.setData(resHash)
                .setCode(HttpStatus.OK.value())
                .setStatus(HttpStatus.OK)
                .setMessage("Authenticated Successfully");
        return new ResponseEntity<>(res, res.getStatus());
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Object> refreshToken() {
        return new ResponseEntity<>("Not Yet implemented", HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/createAccount")
    public ResponseEntity<Object> createAccount(@RequestBody CreateAccountModel accountData) {
        if (accountData.getPassword().length() > 20 || accountData.getPassword().length() < 6) {
            var res = new ExceptionModel();
            res
                    .setTimestamp(Timestamp.from(Instant.now()))
                    .setMessage("Password must be between 6 and 20 characters")
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setCode(HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(res, res.getStatus());
        }
        Role userRole = roleRepository.findById(Role.USER).orElseThrow(() -> new EntityNotFoundException("Role USER is not found"));
        List<Role> roles = new ArrayList<>();
        roles.add(userRole);
        User user = new User();
        user.setUsername(accountData.getUsername());
        user.setEmail(accountData.getEmail());
        user.setPassword(passwordEncoder.encode(accountData.getPassword()));
        user.setRoles(roles);
        var u = userRepository.save(user);
        HashMap<String, Object> resHash = new HashMap<>(4);
        resHash.put("id", u.getId());
        resHash.put("email", u.getEmail());
        resHash.put("username", u.getUsername());
        resHash.put("roles", u.getRoles().stream().map(Role::getName).toList());
        var res = new ResponseObjModel();
        res.setData(resHash)
                .setCode(HttpStatus.CREATED.value())
                .setStatus(HttpStatus.CREATED)
                .setMessage("Account Created");
        return new ResponseEntity<>(res, res.getStatus());
    }
}
