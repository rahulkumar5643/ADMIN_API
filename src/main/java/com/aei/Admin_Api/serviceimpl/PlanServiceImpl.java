package com.aei.Admin_Api.serviceimpl;

import com.aei.Admin_Api.binding.PlanForm;
import com.aei.Admin_Api.constant.AppConstant;
import com.aei.Admin_Api.entities.PlanEntity;
import com.aei.Admin_Api.entities.UserEntity;
import com.aei.Admin_Api.repository.PlanRepo;
import com.aei.Admin_Api.repository.UserRepo;
import com.aei.Admin_Api.service.PlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    private PlanRepo planRepo;
    @Autowired
    private UserRepo userRepo;
    @Override
    public boolean createPlan(PlanForm planForm) {

        try {
            PlanEntity planEntity = new PlanEntity();
            BeanUtils.copyProperties(planForm, planEntity);

            // 🔹 Fetch user
            UserEntity user = userRepo.findById(planForm.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 🔹 Set user in plan
            planEntity.setUser(user);

            planEntity.setActiveSw(AppConstant.SET_ACTIVE_STATUS);

            planRepo.save(planEntity);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<PlanForm> fetchPlans() {

        List<PlanEntity> planEntities = planRepo.findAll();
        List<PlanForm> planFormList = new ArrayList<>();

        for(PlanEntity entity:planEntities){
            PlanForm planForm = new PlanForm();
            BeanUtils.copyProperties(entity,planForm);

            planFormList.add(planForm);
        }
        return planFormList;
    }

    @Override
    public PlanForm getPlanById(Integer planId) {
        Optional<PlanEntity> optional = planRepo.findById(planId);
        if(optional.isPresent()){

            PlanEntity planEntity = optional.get();
            PlanForm planForm = new PlanForm();
            BeanUtils.copyProperties(planEntity,planForm);

            return planForm;
        }
        return null;
    }

    @Override
    public String changePlanStatus(Integer planId, String status) {


        int cnt = planRepo.updateAccStatus(planId,status);
        if(cnt>0){
            return AppConstant.PLAN_STATUS_SUCCESSFULL;
        }
        return AppConstant.PLAN_STATUS_UNSUCCESSFULL;
    }
}
