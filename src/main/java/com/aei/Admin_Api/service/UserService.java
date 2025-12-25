package com.aei.Admin_Api.service;

import com.aei.Admin_Api.binding.DashboardCards;
import com.aei.Admin_Api.binding.LoginForm;
import com.aei.Admin_Api.binding.UserAccountForm;

public interface UserService {

    public String login(LoginForm loginForm);
    public boolean recoverPwd(String email);
    public DashboardCards fetchDashboardCardsInfo();
    public UserAccountForm getUserByEmail(String email);

}
