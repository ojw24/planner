package com.ojw.planner.core.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DatePeriod {

    private LocalDate startDate;

    private LocalDate endDate;

    public DatePeriod(LocalDate startDate, LocalDate endDate) {

        this.startDate = startDate;
        this.endDate = endDate;

        if(startDate == null || endDate == null)
            throw new NullPointerException("start date or end date is null");

        if(startDate.isAfter(endDate))
            throw new IllegalArgumentException("start date must be before end date");

    }

}
