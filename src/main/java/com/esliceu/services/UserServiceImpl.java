package com.esliceu.services;

import com.esliceu.entities.User;
import com.esliceu.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUserById(Long userid){
        return userRepo.findById(userid).get();
    }

    public User getUserByEmailAndAuthAndPassword(String email, String auth, String password) {
        return userRepo.findByEmailAndAuthAndPassword(email, auth, password);
    }

    public void userRegister(String username, String email, String auth, String password1) {
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setAuth(auth);
        u.setPassword(password1);
        userRepo.save(u);
    }
}
