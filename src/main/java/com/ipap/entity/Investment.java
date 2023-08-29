package com.ipap.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@Builder
public class Investment {
    @Id
    private String id;
    private String name;
    private Float amount;
    @CreatedDate
    private LocalDateTime createdAt;
}
