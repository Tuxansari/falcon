package com.fameoflight.falcon.filter;

import com.fameoflight.falcon.constants.FalconConstants;
import com.fameoflight.falcon.utils.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Component
public class UserContextFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		final String requestId = getRequestId(httpReq);
		UserContextHolder.getContext().setRequestId(requestId);
		chain.doFilter(request, response);
	}

	private String getRequestId(HttpServletRequest httpReq) {
		if (httpReq.getHeader(FalconConstants.REQUEST_ID) != null)
			return httpReq.getHeader(FalconConstants.REQUEST_ID);
		else
			return UUID.randomUUID().toString().toUpperCase().replace("-", "");
	}

}
