package com.example.project.repositories;

import com.example.project.entities.UserPurchasesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPurchaseRepository extends CrudRepository<UserPurchasesEntity, String> {
    List<UserPurchasesEntity> findAllByBusinessProfileId(String id);
}
