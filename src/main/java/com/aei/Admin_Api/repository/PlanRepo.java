package com.aei.Admin_Api.repository;

import com.aei.Admin_Api.entities.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepo extends JpaRepository<PlanEntity,Integer> {

    @Query("update PlanEntity set activeSw=:status where planId=:planId")
    public Integer updateAccStatus(Integer planId,String status);
}
