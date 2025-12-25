package com.aei.Admin_Api.serviceimpl;

import com.aei.Admin_Api.binding.UnlockAccountForm;
import com.aei.Admin_Api.binding.UserAccountForm;
import com.aei.Admin_Api.constant.AppConstant;
import com.aei.Admin_Api.entities.UserEntity;
import com.aei.Admin_Api.repository.UserRepo;
import com.aei.Admin_Api.service.AccountService;
import com.aei.Admin_Api.utils.EmailUtils;
import com.aei.Admin_Api.utils.PasswordUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EmailUtils emailUtils;
    @Override
    public boolean createUserAccount(UserAccountForm userAccountForm) {

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userAccountForm,userEntity);

        String tempPassword = PasswordUtils.passwordGenerator();

        userEntity.setAccStatus(AppConstant.setAccStatus);
        userEntity.setActiveSw(AppConstant.SET_ACTIVE_STATUS);
        userEntity.setPwd(tempPassword);
        userRepo.save(userEntity);

        String to = userAccountForm.getEmail();
        String subject = AppConstant.EMAIL_SUBJECT;
        String body = readEmailBody("REG_EMAIL_BODY.txt",userEntity);
        emailUtils.sendEmail(to,subject,body);



        return true;
    }

    @Override
    public List<UserAccountForm> fetchUserAccounts() {

        List<UserEntity> userEntities = userRepo.findAll();

        List<UserAccountForm> users = new ArrayList<>();

        for(UserEntity userEntity : userEntities){

            UserAccountForm user = new UserAccountForm();
            BeanUtils.copyProperties(userEntity,user);

                users.add(user);
        }


        return users;
    }

    @Override
    public UserAccountForm getUserAccById(Long accId) {

        Optional<UserEntity> optional = userRepo.findById(accId);

        if(optional.isPresent()){

            UserEntity userEntity = optional.get();
            UserAccountForm userAccountForm = new UserAccountForm();

            BeanUtils.copyProperties(userEntity,userAccountForm);
            return userAccountForm;

        }

        return null;
    }

    @Override
    public String changeAccStatus(Long userId, String status) {

        int cnt = userRepo.updateAccStatus(userId,status);
        if(cnt>0){
            return AppConstant.PLAN_STATUS_SUCCESSFULL;
        }
        return AppConstant.PLAN_STATUS_UNSUCCESSFULL;
    }

    @Override
    public String unlockUserAccount(UnlockAccountForm unlockAccountForm) {

        String email = unlockAccountForm.getEmail();

        UserEntity userEntity = userRepo.findByEmail(email);



        if(!userEntity.getPwd().equals(unlockAccountForm.getTempPwd())){

            return AppConstant.TEMP_PASSWORD_MSG;

        }
        if(!unlockAccountForm.getNewPwd().equals(unlockAccountForm.getConfirmPwd())){

            return AppConstant.NEW_CONFIRM_PASSWORD_MSG;
        }
        if(userEntity == null){

            return AppConstant.INVALID_EMAIL_MSG;
        }

        userEntity.setPwd(unlockAccountForm.getConfirmPwd());
        userEntity.setAccStatus(AppConstant.setAccStatus);
        userRepo.save(userEntity);
        return AppConstant.SET_UNLOCKED_ACC_MSG;
    }

    private String readEmailBody(String fileName, UserEntity user) {

        StringBuilder sb = new StringBuilder();

        try (
                InputStream is = getClass()
                        .getClassLoader()
                        .getResourceAsStream(fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(is))
        ) {

            if (is == null) {
                throw new RuntimeException("Email template not found: " + fileName);
            }

            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace(AppConstant.FNAME, user.getFullName());
                line = line.replace(AppConstant.PWD, user.getPwd());
                line = line.replace(AppConstant.EMAIL, user.getEmail());
                sb.append(line).append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


}
