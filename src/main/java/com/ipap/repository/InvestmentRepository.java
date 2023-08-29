package com.ipap.repository;

import com.ipap.entity.Investment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface InvestmentRepository extends MongoRepository<Investment, String> {
    List<Investment> findAllByAmountGreaterThan(Float amount);
    Optional<Investment> findByName(String name);
}
