package baris.RoleBasedAuthBackEnd.service;

import baris.RoleBasedAuthBackEnd.model.AuthenticationResponse;
import baris.RoleBasedAuthBackEnd.model.User;
import baris.RoleBasedAuthBackEnd.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
            private final UserRepository userRepository;
            private final PasswordEncoder passwordEncoder;
            private final JwtService  jwtService;
            private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
      //  this.passwordEncoder = passwordEncoder;
        this.passwordEncoder=new BCryptPasswordEncoder();
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User request)
    {
        Optional<User> existingUser=userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent())
        {
            User user= existingUser.get();
            String token=jwtService.generateToken(user);
            return new AuthenticationResponse(token);
        }
        String encodedPassword=passwordEncoder.encode(request.getPassword());
        User user= new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(encodedPassword);
        user.setRole(request.getRole());

        user=userRepository.save(user);
        String token=jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }
    public AuthenticationResponse authenticationResponse(User request)
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }
}
