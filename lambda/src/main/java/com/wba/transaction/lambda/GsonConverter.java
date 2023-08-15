package com.wba.transaction.lambda;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GsonConverter {
    private static final DateTimeFormatter LOCALDATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final DateTimeFormatter LOCALDATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Gson gson;

    public GsonConverter() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, createLocalDateTimeSerializer())
                .registerTypeAdapter(LocalDate.class, createLocalDateSerializer())
                .setPrettyPrinting()
                .create();
    }

    private JsonSerializer<LocalDateTime> createLocalDateTimeSerializer() {
        return (localDateTime, type, jsonSerializationContext) ->
                new JsonPrimitive(LOCALDATETIME_FORMATTER.format(localDateTime));
    }

    private JsonSerializer<LocalDate> createLocalDateSerializer() {
        return (localDate, type, jsonSerializationContext) ->
                new JsonPrimitive(LOCALDATE_FORMATTER.format(localDate));
    }

    <T> String toJson(List<T> list) {
        return gson.toJson(list);
    }
}
