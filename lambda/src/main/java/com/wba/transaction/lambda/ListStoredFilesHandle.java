package com.wba.transaction.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;

import com.wba.transactions.service.s3.FileDao;
import com.wba.transactions.service.s3.FileService;

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

    private static final GsonConverter gson = new GsonConverter();

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
