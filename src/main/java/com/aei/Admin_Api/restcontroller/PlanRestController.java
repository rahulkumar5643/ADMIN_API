package com.aei.Admin_Api.restcontroller;

import com.aei.Admin_Api.binding.PlanForm;
import com.aei.Admin_Api.constant.AppConstant;
import com.aei.Admin_Api.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
public class PlanRestController {

    @Autowired
    private PlanService planService;

    // CREATE PLAN
    @PostMapping("/createPlan")
    public ResponseEntity<String> createPlan(@RequestBody PlanForm planForm) {

        boolean isCreated = planService.createPlan(planForm);

        if (isCreated) {
            return new ResponseEntity<>(AppConstant.PLAN_STATUS_SUCCESSFULL, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(AppConstant.PLAN_STATUS_UNSUCCESSFULL, HttpStatus.BAD_REQUEST);
    }

    // GET ALL PLANS
    @GetMapping("/getAllPlans")
    public ResponseEntity<List<PlanForm>> getAllPlans() {

        List<PlanForm> plans = planService.fetchPlans();
        return new ResponseEntity<>(plans, HttpStatus.OK);
    }

    // GET PLAN BY ID
    @GetMapping("/{id}")
    public ResponseEntity<PlanForm> getPlanById(@PathVariable Integer id) {

        PlanForm planForm = planService.getPlanById(id);
        return new ResponseEntity<>(planForm, HttpStatus.OK);
    }

    // CHANGE PLAN STATUS
    @PutMapping("/{id}/status")
    public ResponseEntity<String> changePlanStatus(@PathVariable Integer id,
                                                   @RequestParam String status) {

        String result = planService.changePlanStatus(id, status);

        if (AppConstant.PLAN_STATUS_SUCCESSFULL.equals(result)) {
            return new ResponseEntity<>(AppConstant.PLAN_STATUS_SUCCESSFULL, HttpStatus.OK);
        }

        return new ResponseEntity<>(AppConstant.PLAN_STATUS_UNSUCCESSFULL, HttpStatus.NOT_FOUND);
    }
}
