package com.wba.transactions.service.s3;

import ch.wba.accounting.banana.BananaTransactionDto;
import ch.wba.accounting.banana.BananaTransactionReader;
import software.amazon.awssdk.services.s3.model.ObjectVersion;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileService {
    private S3Provider s3Provider = new S3Provider(S3_STORE_NAME);
    private BananaTransactionReader bananaReader;

    private static final String S3_STORE_NAME = "wba-file-store";

    public List<FileDao> listStoredFiles() {
        return s3Provider.listObjects()
                .stream()
                .map(this::toFileDao)
                .collect(Collectors.toList());
    }

    protected BananaTransactionReader getBananaReader() {
        if (bananaReader == null) {
            bananaReader = new BananaTransactionReader();
        }
        return bananaReader;
    }

    private FileDao toFileDao(S3Object o) {
        final List<ObjectVersion> versions = s3Provider.listObjectVersion(o.key());
        try {
            BufferedReader bufferedReader = s3Provider.readFile(o.key());
            final List<BananaTransactionDto> bananaTransactionDtos = getBananaReader().readTransactions(bufferedReader);

            final LocalDate fromDate = bananaTransactionDtos.stream()
                    .map(BananaTransactionDto::getDate)
                    .filter(Objects::nonNull)
                    .min(Comparator.naturalOrder())
                    .orElse(null);

            final LocalDate untilDate = bananaTransactionDtos.stream()
                    .map(BananaTransactionDto::getDate)
                    .filter(Objects::nonNull)
                    .max(Comparator.naturalOrder())
                    .orElse(null);

            return FileDao.builder()
                    .name(o.key())
                    .size(bananaTransactionDtos.size())
                    .created(o.lastModified())
                    .versions(versions.size())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
