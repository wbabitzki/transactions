package com.wba.transactions.service.s3;

import ch.wba.accounting.banana.BananaTransactionReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class FileServiceTest {

    private FileService testee = new FileService();

    @Test
    void getBananaReader() {
        assertInstanceOf(BananaTransactionReader.class, testee.getBananaReader());
    }
}