package com.fameoflight.falcon.model;

import com.fameoflight.falcon.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class RequestResponseLoggerObject {

    private String datetime;
    private String env;
    private String requestId;
    private String eventType;
    private String requestType;
    private String resource;
    private String methodType;
    private Map<String, String> headers;
    private String request;
    private String response;
    private String status;
    private Long totalDuration;

    @Override
    public String toString() {
        return CommonUtils.getStrFromObj(this);
    }
}
