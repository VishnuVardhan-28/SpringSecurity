package com.example.basicUserMicroservice.UserDemo.Controller;

import com.example.basicUserMicroservice.UserDemo.Entity.User;
import com.example.basicUserMicroservice.UserDemo.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(HttpServletRequest httpServletRequest){
        return (CsrfToken) httpServletRequest.getAttribute("_csrf");
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/user/{id}")
    public Optional<User> getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/user/email/{email}")
    public Optional<User> getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }
}
