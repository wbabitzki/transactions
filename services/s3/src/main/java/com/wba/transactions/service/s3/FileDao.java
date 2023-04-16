package com.wba.transactions.service.s3;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class FileDao {
    String name;
    LocalDateTime lastModified;
    Long size;
    Integer versions;

    public FileDao(String name, Long size, Instant created, int versions) {
        this.name = name;
        this.size = size;
        this.lastModified = LocalDateTime.ofInstant(created, ZoneOffset.UTC);
        this.versions = versions;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public long getSize() {
        return size != null ? size : 0;
    }

    public int getVersions() {
        return versions;
    }

    @Override
    public String toString() {
        return "FileDao{" +
                "name='" + name + '\'' +
                ", lastModified=" + lastModified +
                ", size=" + size +
                ", versions=" + versions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FileDao)) {
            return false;
        }
        FileDao other = (FileDao) o;
        return Objects.equals(name, other.name)
                && Objects.equals(lastModified, other.lastModified)
                && Objects.equals(size, other.size)
                && Objects.equals(versions, other.versions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastModified, size, versions);
    }
}
