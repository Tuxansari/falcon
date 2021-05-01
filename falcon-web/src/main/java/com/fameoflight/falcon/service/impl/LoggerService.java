package com.fameoflight.falcon.service.impl;

import com.fameoflight.falcon.model.RequestResponseLoggerObject;
import com.fameoflight.falcon.service.ILoggerService;
import com.fameoflight.falcon.utils.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static com.fameoflight.falcon.constants.FalconConstants.*;


@Slf4j
@Service
public class LoggerService implements ILoggerService {

    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public void logSyncFlowRequest(ContentCachingRequestWrapper requestWrapper) {
        RequestResponseLoggerObject requestObject = RequestResponseLoggerObject.builder()
                                                                                .env(env)
                                                                                .requestId(UserContextHolder.getContext().getRequestId())
                                                                                .eventType(REQUEST)
                                                                                .resource(requestWrapper.getRequestURI())
                                                                                .methodType(requestWrapper.getMethod())
                                                                                .headers(getRequestHeaders(requestWrapper))
                                                                                .request(readRequestEntity(requestWrapper))
                                                                                .build();
        log.info(requestObject.toString());
    }

    @Override
    public void logSyncFlowResponse(ContentCachingResponseWrapper responseWrapper, String requestUri) {
        RequestResponseLoggerObject responseObject = RequestResponseLoggerObject.builder()
                .env(env)
                .requestId(UserContextHolder.getContext().getRequestId())
                .eventType(RESPONSE)
                .resource(requestUri)
                .headers(getResponseHeader(responseWrapper))
                .response(readResponseEntity(responseWrapper))
                .status(String.valueOf(responseWrapper.getStatus()))
                .totalDuration(calculateLatency(START_TIME))
                .build();
        log.info(responseObject.toString());
        MDC.clear();
    }


    private String readRequestEntity(ContentCachingRequestWrapper requestWrapper) {
        return new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
    }

    private String readResponseEntity(ContentCachingResponseWrapper responseWrapper) {
        return new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
    }

    private Map<String, String> getRequestHeaders(HttpServletRequest httpRequest) {
        try {
            return Collections.list(httpRequest.getHeaderNames()).stream()
                    .collect(Collectors.toMap(name -> name, httpRequest::getHeader));
        } catch (Exception ex) {
            log.error("Error while fetching request header for request exception ()" , ex);
            return Collections.emptyMap();
        }

    }

    private Map<String, String> getResponseHeader(HttpServletResponse httpResponse) {
        try {
            return httpResponse.getHeaderNames().stream()
                    .collect(Collectors.toMap(key -> key, httpResponse::getHeader, (key1, key2) -> key1));
        } catch (Exception ex) {
            log.error("Error while fetching response header for request exception ()" , ex);
            return Collections.emptyMap();
        }
    }

    private long calculateLatency(String startTimeKey) {
        long executionTime = -1;
        try {
            String stTime = MDC.get(startTimeKey);
            if (!StringUtils.isEmpty(stTime)) {
                executionTime = System.currentTimeMillis() - Long.parseLong(stTime);
            }
        } catch (Exception ex) {
            log.error("Error while calculating latency for request exception ()" , ex);
        }
        return executionTime;
    }
}
