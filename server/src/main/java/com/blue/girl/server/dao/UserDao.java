package com.blue.girl.server.dao;

import com.blue.girl.server.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<UserEntity,Integer> {
    UserEntity findByOpenId(String openId);
    UserEntity findByUnionId(String unionId);
    UserEntity findById(int id);
}
