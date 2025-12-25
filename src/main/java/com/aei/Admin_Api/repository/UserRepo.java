package com.aei.Admin_Api.repository;

import com.aei.Admin_Api.entities.UserEntity;
import jdk.jfr.Registered;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserEntity,Long> {

    @Query("update UserEntity set accStatus=:status where userId=:userId")
    public Integer updateAccStatus(Long userId,String status);
    public UserEntity findByEmail(String email);
    public UserEntity findByEmailAndPwd(String email, String password);
}
