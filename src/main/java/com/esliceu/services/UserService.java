package com.esliceu.services;

import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public interface UserService {
    short checkRegisterCredentials(String username, String email, String password1, String password2, String authMethod) throws Exception;
    short checkLoginCredentials(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
