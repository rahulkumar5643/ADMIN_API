package com.aei.Admin_Api.service;

import com.aei.Admin_Api.binding.UnlockAccountForm;
import com.aei.Admin_Api.binding.UserAccountForm;

import java.util.List;

public interface AccountService {

    public boolean createUserAccount(UserAccountForm userAccountForm);
    public List<UserAccountForm> fetchUserAccounts();
    public UserAccountForm getUserAccById(Long accId);
    public String changeAccStatus(Long accId, String status);
    public String unlockUserAccount(UnlockAccountForm unlockAccountForm);
}
