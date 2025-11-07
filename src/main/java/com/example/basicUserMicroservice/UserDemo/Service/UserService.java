package com.example.basicUserMicroservice.UserDemo.Service;

import com.example.basicUserMicroservice.UserDemo.Entity.User;
import com.example.basicUserMicroservice.UserDemo.Entity.UserPrinciple;
import com.example.basicUserMicroservice.UserDemo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final AuthenticationConfiguration authenticationConfiguration;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private final UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    public UserService(AuthenticationConfiguration authenticationConfiguration, UserRepository userRepository) throws Exception {
        this.authenticationConfiguration = authenticationConfiguration;
        this.userRepository = userRepository;
    }

    public User saveUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("user not found");
        }
        return new UserPrinciple(user);
    }

    public String verify(User user) {
        try {
            AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            return authentication.isAuthenticated() ? jwtService.generateToken(user.getUsername()) : "Failed";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }
}
