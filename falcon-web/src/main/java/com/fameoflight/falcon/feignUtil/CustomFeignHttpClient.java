package com.fameoflight.falcon.feignUtil;

import com.fameoflight.falcon.model.CatalogClientConfig;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomFeignHttpClient {

    @Autowired
    private CatalogClientConfig catalogClientConfig;

    @Bean(name = "catalogHttpClient")
    public ApacheHttpClient apacheHttpClient() {

        Integer socketTimeout = catalogClientConfig.getSocketTimeout();
        Integer connectTimeout = catalogClientConfig.getConnectionTimeout();
        Integer maxConnections = catalogClientConfig.getMaxTotalConnections();

        final RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectTimeout)
                .build();

        final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(maxConnections);
        connManager.setDefaultMaxPerRoute(maxConnections);

        CloseableHttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .setConnectionManager(connManager)
                .build();

        return new ApacheHttpClient(client);
    }
}
