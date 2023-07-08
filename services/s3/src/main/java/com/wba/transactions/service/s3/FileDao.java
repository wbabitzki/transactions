package com.wba.transactions.service.s3;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class FileDao {
    String name;
    LocalDateTime lastModified;
    int size;
    Integer versions;

    FileDao(String name, int size, Instant created, int versions) {
        this.name = name;
        this.size = size;
        this.lastModified = LocalDateTime.ofInstant(created, ZoneOffset.UTC);
        this.versions = versions;
    }

    public static FileDaoBuilder builder() {
        return new FileDaoBuilder();
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public int getSize() {
        return size;
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
