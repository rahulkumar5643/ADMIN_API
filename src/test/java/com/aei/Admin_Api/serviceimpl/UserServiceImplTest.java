package com.aei.Admin_Api.serviceimpl;

import com.aei.Admin_Api.binding.LoginForm;
import com.aei.Admin_Api.constant.AppConstant;
import com.aei.Admin_Api.entities.UserEntity;
import com.aei.Admin_Api.repository.EligRepo;
import com.aei.Admin_Api.repository.UserRepo;
import com.aei.Admin_Api.utils.EmailUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private EligRepo eligRepo;

    @Mock
    private EmailUtils emailUtils;

    @InjectMocks
    private UserServiceImpl userService;

    // ✅ LOGIN SUCCESS
    @Test
    void login_success() {

        LoginForm form = new LoginForm();
        form.setEmail("test@gmail.com");
        form.setPwd("test123");

        UserEntity user = new UserEntity();
        user.setEmail("test@gmail.com");
        user.setPwd("test123");
        user.setAccStatus(AppConstant.SET_UNLOCKED_ACC_MSG);
        user.setActiveSw("Y");

        when(userRepo.findByEmailAndPwd("test@gmail.com", "test123"))
                .thenReturn(user);

        String result = userService.login(form);

        assertEquals(AppConstant.LOGIN_SUCCESS, result);
    }

    // ❌ INVALID CREDENTIALS
    @Test
    void login_invalidCredentials() {

        LoginForm form = new LoginForm();
        form.setEmail("wrong@gmail.com");
        form.setPwd("wrong123");

        when(userRepo.findByEmailAndPwd("wrong@gmail.com", "wrong123"))
                .thenReturn(null);

        String result = userService.login(form);

        assertEquals(AppConstant.INVALID_CREDENTIALS, result);
    }
}
