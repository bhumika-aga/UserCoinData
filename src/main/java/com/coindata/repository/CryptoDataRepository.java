package com.coindata.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coindata.model.CryptoData;

@Repository
public interface CryptoDataRepository extends JpaRepository<CryptoData, Long> {
}