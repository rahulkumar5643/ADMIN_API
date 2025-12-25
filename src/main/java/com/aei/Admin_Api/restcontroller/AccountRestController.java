package com.aei.Admin_Api.restcontroller;

import com.aei.Admin_Api.binding.UnlockAccountForm;
import com.aei.Admin_Api.binding.UserAccountForm;
import com.aei.Admin_Api.constant.AppConstant;
import com.aei.Admin_Api.service.AccountService;
import com.aei.Admin_Api.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountRestController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private EmailUtils emailUtils;
    @PostMapping("/userRegisteration")
    public ResponseEntity<String> userRegisteration(@RequestBody UserAccountForm userAccountForm){
        Boolean status =accountService.createUserAccount(userAccountForm);
        if(status){

            return new ResponseEntity<>(AppConstant.ACC_CREATED_CHECK_EMAIL, HttpStatus.OK);
        }
        return new ResponseEntity<>(AppConstant.ACC_NOT_CREATED,HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getData")
    public ResponseEntity<List<UserAccountForm>> fetchUserAccountData(){

        List<UserAccountForm> userAccountForms = accountService.fetchUserAccounts();

        return new ResponseEntity<>(userAccountForms,HttpStatus.OK);
    }

    @GetMapping("/getDataById/{id}")
    public ResponseEntity<UserAccountForm> fetchDataById(@PathVariable Long id){
        UserAccountForm userAccountForm = accountService.getUserAccById(id);

        return new ResponseEntity<>(userAccountForm,HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/status")
    public ResponseEntity<String> changeStatus(@PathVariable Long userId, @PathVariable String status){

        String accStatus = accountService.changeAccStatus(userId,status);

        if(accStatus.equals(AppConstant.PLAN_STATUS_SUCCESSFULL)){

            return new ResponseEntity<>(AppConstant.PLAN_STATUS_SUCCESSFULL,HttpStatus.OK);
        }

        return new ResponseEntity<>(AppConstant.PLAN_STATUS_UNSUCCESSFULL,HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/unlockUserForm")
    public ResponseEntity<String> unlockUserAccount(@RequestBody UnlockAccountForm unlockAccountForm){


        String accountStatus = accountService.unlockUserAccount(unlockAccountForm);

        if(!accountStatus.equals(AppConstant.SET_UNLOCKED_ACC_MSG)){

            return new ResponseEntity<>(accountStatus,HttpStatus.BAD_REQUEST);
        }

            return new ResponseEntity<>(accountStatus,HttpStatus.CREATED);
    }
}
