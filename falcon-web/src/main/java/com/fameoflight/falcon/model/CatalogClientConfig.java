package com.fameoflight.falcon.model;

import com.fameoflight.falcon.utils.CommonUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "catalog")
@EqualsAndHashCode(callSuper = true)
public class CatalogClientConfig extends DownstreamProperties {

    private String baseUrl;
    private String productUrl;

    @Override
    public String toString() {
        return CommonUtils.getStrFromObj(this);
    }
}
