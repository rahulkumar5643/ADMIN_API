package com.aei.Admin_Api.serviceimpl;

import com.aei.Admin_Api.binding.DashboardCards;
import com.aei.Admin_Api.binding.LoginForm;
import com.aei.Admin_Api.binding.UserAccountForm;
import com.aei.Admin_Api.constant.AppConstant;
import com.aei.Admin_Api.entities.EligEntity;
import com.aei.Admin_Api.entities.UserEntity;
import com.aei.Admin_Api.repository.EligRepo;
import com.aei.Admin_Api.repository.UserRepo;
import com.aei.Admin_Api.service.UserService;
import com.aei.Admin_Api.utils.EmailUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EligRepo eligRepo;
    @Autowired
    private EmailUtils emailUtils;
    @Override
    public String login(LoginForm loginForm) {

        UserEntity userEntity = userRepo.findByEmailAndPwd(loginForm.getEmail(),loginForm.getPwd());

        if(userEntity == null){

            return AppConstant.INVALID_CREDENTIALS;
        }
       if(AppConstant.SET_UNLOCKED_ACC_MSG.equals(userEntity.getAccStatus()) && "Y".equals(userEntity.getActiveSw())){

           return AppConstant.LOGIN_SUCCESS;
       }else {

           return AppConstant.ACCOUNT_LOCKED_STATUS;
       }
    }

    @Override
    public boolean recoverPwd(String email) {

        UserEntity userEmail = userRepo.findByEmail(email);

        if(userEmail == null){

            return false;
        }else{

            String subject = AppConstant.PASS_EMAIL_SUBJECT;
            String body = "Your RecoverPassword is: "+ userEmail.getPwd();

            return emailUtils.sendEmail(email,subject,body);
        }
    }

    @Override
    public DashboardCards fetchDashboardCardsInfo() {

        long plansCount = eligRepo.count();
        List<EligEntity> eligEntityList = eligRepo.findAll();

        Long approvedCnt = eligEntityList.stream().filter(ed -> ed.getPlanStatus().equals("APPROVED")).count();
        Long deniedCnt = eligEntityList.stream().filter(ed -> ed.getPlanStatus().equals("DENIED")).count();
        Double total = eligEntityList.stream().mapToDouble(ed-> ed.getBenefitAmount()).sum();

        DashboardCards card = new DashboardCards();
        card.setPlansCnt(plansCount);
        card.setApprovedCnt(approvedCnt);
        card.setDeniedCnt(deniedCnt);
        card.setBeniftAmtGiven(total);


        return card;
    }

    @Override
    public UserAccountForm getUserByEmail(String email) {
        UserEntity userEntity = userRepo.findByEmail(email);
        UserAccountForm userAccountForm = new UserAccountForm();
        BeanUtils.copyProperties(userEntity,userAccountForm);
        return userAccountForm;
    }
}
