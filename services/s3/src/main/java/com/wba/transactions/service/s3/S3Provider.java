package com.wba.transactions.service.s3;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectVersionsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ObjectVersion;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class S3Provider {
    private S3Client s3;

    final private String bucket;

    public S3Provider(String bucket) {
        this.bucket = bucket;
    }

    protected S3Client getS3Client() {
        if (s3 == null) {
            s3 = S3Client.builder()
                    .region(Region.EU_CENTRAL_1)
                    .build();
        }
        return s3;
    }

    protected void setS3Client(S3Client s3Client) {
        this.s3 = s3Client;
    }

    public List<S3Object> listObjects() {
        ListObjectsRequest request = ListObjectsRequest.builder()
                .bucket(bucket)
                .build();
        return getS3Client().listObjects(request).contents();
    }

    public List<ObjectVersion> listObjectVersion(String key) {
        final ListObjectVersionsRequest request = ListObjectVersionsRequest.builder()
                .bucket(bucket)
                .prefix(key)
                .build();
        return getS3Client().listObjectVersions(request).versions();
    }

    public BufferedReader readFile (String file) throws IOException  {
        final GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(file)
                .build();
        try (ResponseInputStream<GetObjectResponse> response = getS3Client().getObject(request)) {
            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response.readAllBytes())));
        }
    }
}
