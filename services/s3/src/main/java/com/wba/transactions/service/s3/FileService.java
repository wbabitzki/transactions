package com.wba.transactions.service.s3;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectVersionsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectVersionsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;
import java.util.stream.Collectors;

public class FileService {
    private S3Client s3;

    private static final String S3_STORE_NAME = "wba-file-store";

    private FileDao toFileDao(S3Object o) {
        final ListObjectVersionsResponse versionsResponse = listObjectVersion(o);
        return new FileDao(o.key(), o.size(), o.lastModified(), versionsResponse.versions().size());
    }

    protected S3Client getS3() {
        if (s3 == null) {
            s3 = S3Client.builder()
                    .region(Region.EU_CENTRAL_1)
                    .build();
        }
        return s3;
    }

    public List<FileDao> listStoredFiles() {
        return listObjects(S3_STORE_NAME)
                .contents()
                .stream()
                .map(this::toFileDao)
                .collect(Collectors.toList());
    }

    private ListObjectVersionsResponse listObjectVersion(S3Object o) {
        final ListObjectVersionsRequest request = ListObjectVersionsRequest.builder()
                .bucket(S3_STORE_NAME)
                .prefix(o.key())
                .build();
        return getS3().listObjectVersions(request);
    }

    private ListObjectsResponse listObjects(String bucket) {
        ListObjectsRequest request = ListObjectsRequest.builder()
                .bucket(bucket)
                .build();
        return getS3().listObjects(request);
    }
}
