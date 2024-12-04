package com.example.project.repositories;

import com.example.project.entities.UserPurchasesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPurchaseRepository extends CrudRepository<UserPurchasesEntity, String> {
}
