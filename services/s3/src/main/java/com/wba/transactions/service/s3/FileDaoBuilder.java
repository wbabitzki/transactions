package com.wba.transactions.service.s3;

import java.time.Instant;
import java.time.LocalDate;

final public class FileDaoBuilder {
    private String name;
    private int size;
    private Instant created;
    private int versions;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public FileDaoBuilder name(String name) {
        this.name = name;
        return this;
    }

    public FileDaoBuilder size(int size) {
        this.size = size;
        return this;
    }

    public FileDaoBuilder created(Instant created) {
        this.created = created;
        return this;
    }

    public FileDaoBuilder versions(int versions) {
        this.versions = versions;
        return this;
    }

    public FileDaoBuilder dateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public FileDaoBuilder dateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public FileDao build() {
        final FileDao result = new FileDao(name, size, created, versions);
        result.setDateFrom(dateFrom);
        result.setDateTo(dateTo);
        return result;
    }
}
