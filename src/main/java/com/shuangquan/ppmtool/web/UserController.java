package com.shuangquan.ppmtool.web;

import com.shuangquan.ppmtool.domain.User;
import com.shuangquan.ppmtool.services.MapValidationErrorService;
import com.shuangquan.ppmtool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result){
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;
        User newUser = userService.saveUser(user);
        return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }
}
