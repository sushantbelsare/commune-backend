package com.cmn.commune_backend.controller;

import com.cmn.commune_backend.entity.User;
import com.cmn.commune_backend.model.response.LoginResponse;
import com.cmn.commune_backend.model.response.SignUpResponse;
import com.cmn.commune_backend.repository.UserRepository;
import com.cmn.commune_backend.topic.OnboardingTopic;
import com.cmn.commune_backend.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(path = "${api_version}")
public class AuthController {

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    KafkaTemplate<String, OnboardingTopic> onboardingKafkaTemplate;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody User request){
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        authenticationManager.authenticate(token);

        String jwt = jwtTokenUtil.generateToken(request.getUsername());

        onboardingKafkaTemplate.send("onboarding", new OnboardingTopic(request.getUsername(), "/login", new Date()));

        return ResponseEntity.ok(new LoginResponse("Authorized", "Bearer "+jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<SignUpResponse> signup(@RequestBody User request){
        request.setPassword(encoder.encode(request.getPassword()));

        userRepository.save(request);

        return ResponseEntity.ok(new SignUpResponse("Registered Successfully!"));
    }

}
