package org.example.service;

import org.example.dto.AuthResponse;
import org.example.dto.SignInRequest;
import org.example.dto.SignUpRequest;
import org.example.model.Bio;
import org.example.model.Profile;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse signup(SignUpRequest request) {
        var bio = new Bio();
        bio.setDescription("New user");
        bio.setInterests("Not specified");

        var profile = new Profile();
        profile.setFirstName("New");
        profile.setLastName("User");
        profile.setBio(bio);
        bio.setProfile(profile);

        var user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProfile(profile);
        profile.setUser(user);
        
        userRepository.save(user);
        
        var jwt = jwtService.generateToken(user);
        return new AuthResponse(jwt);
    }

    public AuthResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        
        var jwt = jwtService.generateToken(user);
        return new AuthResponse(jwt);
    }
} 