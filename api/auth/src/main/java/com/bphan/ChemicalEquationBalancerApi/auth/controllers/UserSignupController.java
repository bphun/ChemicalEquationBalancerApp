package com.bphan.ChemicalEquationBalancerApi.auth.controllers;

import com.bphan.ChemicalEquationBalancerApi.auth.jdbc.UserRepository;
import com.bphan.ChemicalEquationBalancerApi.auth.models.AuthenticationResponse;
import com.bphan.ChemicalEquationBalancerApi.common.auth.AppUser;
import com.bphan.ChemicalEquationBalancerApi.common.auth.UserCredentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserSignupController {

    @Autowired
    private UserRepository userRepository;
    
    @CrossOrigin(origins = "*")
    @PostMapping(value="/auth/signup")
    public AuthenticationResponse signup(@RequestBody UserCredentials credentials) {
        return userRepository.createUser(new AppUser(credentials));
    }
    

}