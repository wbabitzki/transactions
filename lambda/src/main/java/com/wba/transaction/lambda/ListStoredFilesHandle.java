package com.wba.transaction.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.wba.transactions.service.s3.FileDao;
import com.wba.transactions.service.s3.FileService;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


public class ListStoredFilesHandle implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

    protected ListStoredFilesHandle(FileService service) {
        this.service = service;
    }

    public ListStoredFilesHandle() {
        this.service = new FileService();
    }

    private FileService service;

    private static final JsonSerializer<LocalDateTime> LOCALDATETIME_SERIALIZER = new JsonSerializer<>() {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(formatter.format(localDateTime));
        }
    };

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, LOCALDATETIME_SERIALIZER)
            .setPrettyPrinting()
            .create();

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent apiGatewayV2HTTPEvent, Context context) {
        final List<FileDao> files = service.listStoredFiles();

        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(200)
                .withHeaders(Map.of("Content-Type", "application/json"))
                .withBody(gson.toJson(files))
                .build();
    }
}
