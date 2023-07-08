package com.wba.transactions.service.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectVersionsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectVersionsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.ObjectVersion;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class S3ProviderTest {

    private S3Provider testee = new S3Provider("TEST_BUCKET");
    @Mock
    private S3Client s3Client = mock(S3Client.class);

    @BeforeEach
    void setUp() {
        testee.setS3Client(s3Client);
    }

    @Test
    void getS3Client() {
        assertInstanceOf(S3Client.class, new S3Provider("TEST_BUCKET").getS3Client());
    }

    @Test
    void listObjects_bucket_listOfStoredFiles() {
        // arrange
        final List<S3Object> files = Stream.of("file1", "file2", "file3")
                .map(f -> S3Object.builder().key(f).build())
                .collect(Collectors.toList());
        final ListObjectsResponse response = ListObjectsResponse.builder().contents(files).build();
        when(s3Client.listObjects(any(ListObjectsRequest.class))).thenReturn(response);

        // act
        final List<String> result = testee.listObjects().stream()
                .map(S3Object::key)
                .collect(Collectors.toList());

        // assert
        final List<String> resultFileNames = files.stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
        assertIterableEquals(resultFileNames, result);
    }

    @Test
    void listObjectVersion_fileName_versions() {
        // arrange
        final List<ObjectVersion> versions = Stream.of(1, 2, 3)
                .map(a -> ObjectVersion.builder().build())
                .collect(Collectors.toList());
        final ListObjectVersionsResponse response = ListObjectVersionsResponse.builder().versions(versions).build();
        when(s3Client.listObjectVersions(any(ListObjectVersionsRequest.class))).thenReturn(response);

        // act
        List<ObjectVersion> result = testee.listObjectVersion("file");

        // assert
        assertEquals(3, result.size());
    }

    @Test
    void readFile_fileName_content() throws IOException {
        // arrange
        final String testContent = "test text";
        ResponseInputStream<GetObjectResponse> response = mock(ResponseInputStream.class);
        when(response.readAllBytes()).thenReturn(testContent.getBytes());
        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(response);

        // act
        try (BufferedReader result = testee.readFile("test")) {

            // assert
            assertEquals(testContent, result.lines().collect(Collectors.joining()));
        }

        // verify interactions
        verify(s3Client).getObject(any(GetObjectRequest.class));
        verify(response).close();
    }
}