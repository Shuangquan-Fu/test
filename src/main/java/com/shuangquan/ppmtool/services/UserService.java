package com.shuangquan.ppmtool.services;

import com.shuangquan.ppmtool.domain.User;
import com.shuangquan.ppmtool.exceptions.UsernameAlreadyExistsException;
import com.shuangquan.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){
        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            //username must be unique(exception)
            newUser.setUsername(newUser.getUsername());
            //make sure that password and confirmed password match
            // do not persist or show the confirmed password
            return userRepository.save(newUser);

        } catch (Exception e){
            throw new UsernameAlreadyExistsException("username " + newUser.getUsername()+"Alreadly exists!");
        }

    }
}
