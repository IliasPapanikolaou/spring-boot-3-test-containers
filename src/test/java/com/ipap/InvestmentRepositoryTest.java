package com.ipap;

import com.ipap.entity.Investment;
import com.ipap.repository.InvestmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InvestmentRepositoryTest extends ContainerBase {

    @Autowired
    private InvestmentRepository investmentRepository;

    @Test
    @DisplayName("Create investment")
    @Order(1)
    void testCreate() {
        var investment = Investment.builder()
                .name("Buy some bitcoin")
                .amount(1000f)
                .build();
        Investment savedInvestment = investmentRepository.save(investment);

        Assertions.assertNotNull(savedInvestment.getId());
    }

    @Test
    @DisplayName("Find investment by name")
    @Order(2)
    void testFind() {
        Optional<Investment> result = investmentRepository.findByName("Buy some bitcoin");

        Assertions.assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Find investments greater than specified amount")
    @Order(3)
    void testFindAllByAmountGreaterThan() {
        investmentRepository.save(Investment.builder()
                .name("Buy some bitcoin")
                .amount(100f)
                .build()
        );

        investmentRepository.save(Investment.builder()
                .name("Buy some bitcoin")
                .amount(600f)
                .build()
        );
        List<Investment> result = investmentRepository.findAllByAmountGreaterThan(500f);

        Assertions.assertEquals(2, result.size());
    }

}
