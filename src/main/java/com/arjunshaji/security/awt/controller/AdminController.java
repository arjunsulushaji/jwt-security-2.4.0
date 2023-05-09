package com.arjunshaji.security.awt.controller;

import com.arjunshaji.security.awt.model.JWTRequest;
import com.arjunshaji.security.awt.model.JWTResponse;
import com.arjunshaji.security.awt.service.UserService;
import com.arjunshaji.security.awt.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;


    @GetMapping("/home")
    public String adminHome(){
        return "WELCOME TO ADMIN HOME";
    }

    @PostMapping("/authenticate")
    public JWTResponse authenticate(@RequestBody JWTRequest jwtRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e){
            throw new Exception("INVALID CREDENTIALS",e);
        }
        final UserDetails userDetails =
                userService.loadUserByUsername(jwtRequest.getUsername());
        final String token =
                jwtUtility.generateToken(userDetails);
        return new JWTResponse(token);

    }
}
