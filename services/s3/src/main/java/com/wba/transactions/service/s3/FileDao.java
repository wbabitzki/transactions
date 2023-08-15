package com.wba.transactions.service.s3;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class FileDao {
    private String name;
    private LocalDateTime lastModified;
    private int size;
    private Integer versions;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    FileDao(String name, int size, Instant created, int versions) {
        this.name = name;
        this.size = size;
        this.lastModified = created != null ? LocalDateTime.ofInstant(created, ZoneOffset.UTC) : null;
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

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        return "FileDao{" +
                "name='" + name + '\'' +
                ", lastModified=" + lastModified +
                ", size=" + size +
                ", versions=" + versions +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
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
                && Objects.equals(versions, other.versions)
                && Objects.equals(dateFrom, other.dateFrom)
                && Objects.equals(dateTo, other.dateTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastModified, size, versions);
    }
}
