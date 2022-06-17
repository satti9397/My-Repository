package com.demo.paymenttransfer.repository;

import com.demo.paymenttransfer.model.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Integer> {
    //List<TransactionDetails> findAllBySenderIdOrReceiverId(int accountId);

    @Query(value = "select * from transaction_details where sender_id=:accountId or receiver_id=:accountId order by id desc limit 20", nativeQuery = true)
    List<TransactionDetails> findTransactionsForAccount(@Param("accountId") int accountId);
}