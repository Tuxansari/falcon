package com.fameoflight.falcon.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class CommonUtils {

    private static final ObjectMapper mapper = new ObjectMapper()
                                                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                                                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static String getStrFromObj(Object obj) {
        String data = null;
        try {
            data = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
        }
        return data;
    }
}
