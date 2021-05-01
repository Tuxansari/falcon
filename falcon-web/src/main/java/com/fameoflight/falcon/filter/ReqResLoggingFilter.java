package com.fameoflight.falcon.filter;

import com.fameoflight.falcon.service.ILoggerService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.fameoflight.falcon.constants.FalconConstants.START_TIME;

@Slf4j
@Component
public class ReqResLoggingFilter implements Filter {

    @Value("${spring.profiles.active}")
    private String env;

    @Autowired
    private ILoggerService loggerService;

    @Override
    public void init(final FilterConfig config) throws ServletException {
        log.debug("Request Response filter initialized.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        MDC.put(START_TIME, String.valueOf(System.currentTimeMillis()));
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            loggerService.logSyncFlowRequest(requestWrapper);
            loggerService.logSyncFlowResponse(responseWrapper, requestWrapper.getRequestURI());
            try {
                responseWrapper.copyBodyToResponse();
            } catch (IOException ex) {
                log.error("Exception occurred while reading response stream :{}", ex.getMessage());
            }
        }
    }

    @Override
    public void destroy() {
        log.debug("Request Response filter destroyed.");
    }

}
