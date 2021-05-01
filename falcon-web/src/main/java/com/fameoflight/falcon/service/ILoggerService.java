package com.fameoflight.falcon.service;

import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

public interface ILoggerService {

    void logSyncFlowRequest(ContentCachingRequestWrapper requestWrapper);

    void logSyncFlowResponse(ContentCachingResponseWrapper responseWrapper, String requestUri);
}
