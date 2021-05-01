package com.fameoflight.falcon.feignUtil;

import com.fameoflight.falcon.model.RequestResponseLoggerObject;
import com.fameoflight.falcon.utils.UserContextHolder;
import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fameoflight.falcon.constants.FalconConstants.REQUEST;
import static com.fameoflight.falcon.constants.FalconConstants.RESPONSE;

@Slf4j
@Component
public class CustomFeignLogger extends Logger {

    @Value("${spring.profiles.active}")
    private String env;

    @Override
    protected void log(String configKey, String format, Object... args) {
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        RequestResponseLoggerObject requestObject = RequestResponseLoggerObject.builder()
                                                                                .env(env)
                                                                                .requestId(UserContextHolder.getContext().getRequestId())
                                                                                .eventType(REQUEST)
                                                                                .requestType(getRequestType(configKey))
                                                                                .resource(request.url())
                                                                                .methodType(request.httpMethod().name())
                                                                                .headers(getHeader(request.headers()))
                                                                                .build();

        if (Request.HttpMethod.POST.name().equals(request.httpMethod().name()))
            requestObject.setRequest(new String(request.body(), StandardCharsets.UTF_8));

        log.info(requestObject.toString());
    }

    @Override
    protected IOException logIOException(String configKey, Level logLevel, IOException ioe, long elapsedTime) {
        RequestResponseLoggerObject responseObject = RequestResponseLoggerObject.builder()
                                                                                .env(env)
                                                                                .requestId(UserContextHolder.getContext().getRequestId())
                                                                                .eventType(RESPONSE)
                                                                                .requestType(getRequestType(configKey))
                                                                                .totalDuration(elapsedTime)
                                                                                .status("IOException")
                                                                                .response(ioe.getMessage())
                                                                                .build();
        log.info(responseObject.toString());
        return super.logIOException(configKey, logLevel, ioe, elapsedTime);
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        HttpStatus responseStatus = Objects.requireNonNull(HttpStatus.resolve(response.status()), "Unable to interpret response status");
        RequestResponseLoggerObject responseObject = RequestResponseLoggerObject.builder()
                                                                                .env(env)
                                                                                .requestId(UserContextHolder.getContext().getRequestId())
                                                                                .eventType(RESPONSE)
                                                                                .requestType(getRequestType(configKey))
                                                                                .resource(response.request().url())
                                                                                .totalDuration(elapsedTime)
                                                                                .headers(getHeader(response.headers()))
                                                                                .status(responseStatus.toString())
                                                                                .build();

        byte[] bodyData = Util.toByteArray(response.body().asInputStream());
        responseObject.setResponse(new String(bodyData, Charset.defaultCharset()));
        log.info(responseObject.toString());
        MDC.clear();
        Response.Builder responseBuilder = response.toBuilder();
        responseBuilder.body(bodyData);
        return responseBuilder.build();
    }

    private Map<String, String> getHeader(Map<String, Collection<String>> headers) {
        Map<String, String> headerMap = new HashMap<>();
        for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            String headerValue = entry.getValue().toString();
            headerMap.put(headerName, headerValue);
        }
        return headerMap;
    }

    private String getRequestType(String configKey) {
        try {
            String str = configKey.split("[(]")[0];
            String clientName = str.split("#")[0];
            String apiName = str.split("#")[1];
            return clientName.replace("Client", "") + apiName.replace("call", "");
        } catch (Exception ex) {
            return StringUtils.EMPTY;
        }
    }
}
