package com.wba.transaction.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.wba.transactions.service.s3.FileDao;
import com.wba.transactions.service.s3.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.http.HttpStatusCode;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListStoredFilesHandleTest {

    private static final JsonDeserializer<LocalDateTime> LOCALDATETIME_DESERIALIZER = new JsonDeserializer<>() {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        @Override
        public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return LocalDateTime.from(formatter.parse(jsonElement.getAsString()));
        }
    };

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, LOCALDATETIME_DESERIALIZER)
            .setPrettyPrinting()
            .create();

    private ListStoredFilesHandle testee;

    @Mock
    private FileService service;

    @Mock
    APIGatewayV2HTTPEvent event;

    @Mock
    Context context;

    @BeforeEach
    void setUp() {
        testee = new ListStoredFilesHandle(service);
    }

    @Test
    public void testHandleRequest() {
        // Arrange
        List<FileDao> files = Arrays.asList(
                FileDao.builder().name("file1").size(500).created(Instant.parse("2022-10-26T12:25:55.000Z")).versions(2).build(),
                FileDao.builder().name("file2").size(1500).created(Instant.parse("2023-01-08T19:42:54.000Z")).versions(5).build());
        when(service.listStoredFiles()).thenReturn(files);

        // Act
        APIGatewayV2HTTPResponse response = testee.handleRequest(event, context);

        // Assert
        assertAll("response",
                () -> assertEquals(HttpStatusCode.OK, response.getStatusCode()),
                () -> assertHeader(response, "Content-Type", "application/json"),
                () -> {
                    String body = response.getBody();
                    List<FileDao> actualFiles = Arrays.asList(gson.fromJson(body, FileDao[].class));
                    assertEquals(files, actualFiles);
                }
        );
    }

    private void assertHeader(APIGatewayV2HTTPResponse response, String headerName, String expectedValue) {
        Map<String, String> headers = response.getHeaders();
        assertTrue(headers.containsKey(headerName));
        assertEquals(expectedValue, headers.get(headerName));
    }
}