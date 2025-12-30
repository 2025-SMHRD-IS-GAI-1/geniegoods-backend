package com.example.geniegoods.repository;

import com.example.geniegoods.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<UserEntity, Long> {

}
