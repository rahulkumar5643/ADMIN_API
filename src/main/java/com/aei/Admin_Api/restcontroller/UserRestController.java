package com.aei.Admin_Api.restcontroller;

import com.aei.Admin_Api.binding.DashboardCards;
import com.aei.Admin_Api.binding.LoginForm;
import com.aei.Admin_Api.binding.UserAccountForm;
import com.aei.Admin_Api.constant.AppConstant;
import com.aei.Admin_Api.entities.UserEntity;
import com.aei.Admin_Api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserRestController{

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {

        String status = userService.login(loginForm);

        if (!AppConstant.LOGIN_SUCCESS.equals(status)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(status);
        }

        DashboardCards cards = userService.fetchDashboardCardsInfo();
        UserAccountForm user = userService.getUserByEmail(loginForm.getEmail());
        cards.setUser(user);

        return ResponseEntity.ok(cards);
    }

}
