package com.wba.transactions.service.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FileDaoBuilderTest {

    private FileDaoBuilder testee;

    @BeforeEach
    void setUp() {
        testee = FileDao.builder();
    }

    @Test
    void dateFrom() {
        // arrange
        final LocalDate date = LocalDate.parse("2023-07-07");
        // act
        final FileDao result = testee
                .dateFrom(date)
                .build();
        // assert
        assertEquals(date, result.getDateFrom());
    }

    @Test
    void dateUntil() {
        // arrange
        final LocalDate date = LocalDate.parse("2023-07-07");
        // act
        final FileDao result = testee
                .dateTo(date)
                .build();
        // assert
        assertEquals(date, result.getDateTo());
    }
}