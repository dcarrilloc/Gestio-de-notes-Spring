package com.esliceu.services;

import org.springframework.stereotype.Service;


public interface UserService {
    short checkRegisterCredentials(String username, String email, String password1, String password2, String authMethod);
}
