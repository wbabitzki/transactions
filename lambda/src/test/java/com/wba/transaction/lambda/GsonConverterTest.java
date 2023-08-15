package com.wba.transaction.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GsonConverterTest {

    private GsonConverter testee;

    @BeforeEach
    void setUp() {
        testee = new GsonConverter();
    }

    @Test
    void toJsonLocalDateTime() {
        // arrange
        List<LocalDateTime> list = List.of(
                LocalDateTime.of(2023, 1, 1, 12, 20),
                LocalDateTime.of(2023, 1, 2, 13, 40));
        // act
        String result = testee.toJson(list);
        // assert
        assertEquals("[\n" +
                "  \"2023-01-01T12:20:00.000Z\",\n" +
                "  \"2023-01-02T13:40:00.000Z\"\n" +
                "]", result);
    }

    @Test
    void toJsonLocalDate() {
        // arrange
        List<LocalDate> list = List.of(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 2));
        // act
        String result = testee.toJson(list);
        // assert
        assertEquals("[\n" +
                "  \"2023-01-01\",\n" +
                "  \"2023-01-02\"\n" +
                "]", result);
    }
}