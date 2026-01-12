package com.aei.Admin_Api.restcontroller;

import com.aei.Admin_Api.binding.DashboardCards;
import com.aei.Admin_Api.binding.LoginForm;
import com.aei.Admin_Api.binding.UserAccountForm;
import com.aei.Admin_Api.constant.AppConstant;
import com.aei.Admin_Api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRestController.class)
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ SUCCESS LOGIN TEST
    @Test
    void login_success_shouldReturnDashboardCards() throws Exception {

        LoginForm loginForm = new LoginForm();
        loginForm.setEmail("test@gmail.com");
        loginForm.setPwd("test123");

        DashboardCards cards = new DashboardCards();

        UserAccountForm user = new UserAccountForm();
        user.setEmail("test@gmail.com");

        when(userService.login(any(LoginForm.class)))
                .thenReturn(AppConstant.LOGIN_SUCCESS);

        when(userService.fetchDashboardCardsInfo())
                .thenReturn(cards);

        when(userService.getUserByEmail(anyString()))
                .thenReturn(user);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email").value("test@gmail.com"));
    }

    // ❌ INVALID LOGIN TEST
    @Test
    void login_failure_shouldReturnUnauthorized() throws Exception {

        LoginForm loginForm = new LoginForm();
        loginForm.setEmail("wrong@gmail.com");
        loginForm.setPwd("wrong123");

        when(userService.login(any(LoginForm.class)))
                .thenReturn("Invalid Credentials");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginForm)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid Credentials"));
    }
}
