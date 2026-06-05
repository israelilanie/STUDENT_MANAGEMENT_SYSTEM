package com.israel.studentmanagementsystem.service;

import com.israel.studentmanagementsystem.dto.request.LoginRequest;
import com.israel.studentmanagementsystem.dto.request.RegisterRequest;
import com.israel.studentmanagementsystem.dto.response.AuthResponse;
import com.israel.studentmanagementsystem.entity.User;
import com.israel.studentmanagementsystem.enums.Role;
import com.israel.studentmanagementsystem.enums.UserStatus;
import com.israel.studentmanagementsystem.exception.BadCredentialsException;
import com.israel.studentmanagementsystem.exception.DuplicateEmailException;
import com.israel.studentmanagementsystem.mapper.UserMapper;
import com.israel.studentmanagementsystem.repository.UserRepository;
import com.israel.studentmanagementsystem.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final StudentService studentService;

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateEmailException(registerRequest.getEmail());
        }

        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .role(Role.ROLE_STUDENT)
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);
        studentService.createProfile(savedUser);
        String token = jwtUtil.generateToken(savedUser.getEmail(),savedUser.getRole().toString());

        return new AuthResponse(token,"Bearer",userMapper.toResponse(savedUser));
    }

    public AuthResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new BadCredentialsException();
        }

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(BadCredentialsException::new);

        String token = jwtUtil.generateToken(user.getEmail(),user.getRole().toString());


        return new AuthResponse(token,"Bearer",userMapper.toResponse(user));

    }
}
