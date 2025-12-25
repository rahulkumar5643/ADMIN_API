package com.aei.Admin_Api.repository;

import com.aei.Admin_Api.entities.EligEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EligRepo extends JpaRepository<EligEntity,Integer> {
}
