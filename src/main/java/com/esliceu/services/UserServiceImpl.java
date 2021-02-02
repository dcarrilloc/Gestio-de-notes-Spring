package com.esliceu.services;

import com.esliceu.entities.User;
import com.esliceu.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;


    public User getUserById(Long userid){
        Optional<User> u = userRepo.findById(userid);
        return u.orElse(null);
    }

    public Long checkIfUserIsLogged(String email, String auth) {
        User user = userRepo.findByEmailAndAuth(email, auth);
        if(user != null) {
            return user.getUserid();
        }
        return null;
    }

    public User getUserByEmailAndAuthAndPassword(String email, String auth, String password) {
        return userRepo.findByEmailAndAuthAndPassword(email, auth, password);
    }

    public Long nativeRegister(String username, String email, String auth, String password1) {
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setAuth(auth);
        u.setPassword(password1);
        return userRepo.save(u).getUserid();
    }

    public Long oAuth2Register(String email, String auth) {
        User u = new User();
        u.setEmail(email);
        u.setAuth(auth);

        String username = email.split("@")[0];
        int counter = 2;
        while(userRepo.findByUsername(username) != null){
            username = email.split("@")[0] + counter;
            counter++;
        }

        u.setUsername(username);

        return userRepo.save(u).getUserid();
    }
}
