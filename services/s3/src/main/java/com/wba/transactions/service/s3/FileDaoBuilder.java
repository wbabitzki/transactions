package com.wba.transactions.service.s3;

import java.time.Instant;

final public class FileDaoBuilder {
    private String name;
    private int size;
    private Instant created;
    private int versions;

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

    public FileDao build() {
        return new FileDao(name, size, created, versions);
    }
}
