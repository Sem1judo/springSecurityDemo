package com.example.springsecuritydemo.model;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;

public enum TariffPlan {
    FREE("Free training", new BigDecimal("0"), LocalDate.now()),
    ONLINE("Online training", new BigDecimal("300"),  LocalDate.now()),
    PERSONAL("Personal Training", new BigDecimal("600"),  LocalDate.now());

    public final String label;
    @Digits(integer = 4, fraction = 2)
    public final BigDecimal price;
    public final LocalDate joinedPlanDate;

    TariffPlan(String label, BigDecimal price, LocalDate joinedPlanDate) {
        this.label = label;
        this.price = price;
        this.joinedPlanDate = joinedPlanDate;
    }

    public String getLabelPlan() {
        return label;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDate getJoinedPlanDate() {
        return joinedPlanDate;
    }
}
