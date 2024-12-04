package com.example.project.repositories;

import com.example.project.entities.TransactionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionsEntity, String> {

    @Query("SELECT t from TransactionsEntity t JOIN FETCH t.userProfile WHERE t.userProfile.id = :userId AND t.paymentStatus = 'PENDING'")
    Optional<TransactionsEntity> findTransactionsOfUserWithPandingStatus(String userId);
}
