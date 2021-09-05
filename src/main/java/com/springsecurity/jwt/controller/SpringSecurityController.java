package com.springsecurity.jwt.controller;

import com.springsecurity.jwt.models.AuthenticationRequest;
import com.springsecurity.jwt.models.AuthenticationResponse;
import com.springsecurity.jwt.models.User;
import com.springsecurity.jwt.services.MyUserDetailService;
import com.springsecurity.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringSecurityController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    UserDetails userDetails;

    @RequestMapping({"/hello"})
    public String hello() {
        return "Hello World!";
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        userDetails = userDetailService.loadUserByUsername(authenticationRequest.getUsername());
        if (userDetails.getUsername() == authenticationRequest.getUsername() && userDetails.getPassword() == authenticationRequest.getPassword()) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        } else {
            throw new BadCredentialsException("Incorrect Username Or Password");
        }

        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public String createUser(@RequestBody User user) throws Exception {
        try {
            userDetailService.createUser(user);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "User successfully created";
    }
}
