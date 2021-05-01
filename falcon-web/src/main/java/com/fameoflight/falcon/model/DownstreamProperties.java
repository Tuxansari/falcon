package com.fameoflight.falcon.model;

import com.fameoflight.falcon.utils.CommonUtils;
import lombok.Data;

@Data
public abstract class DownstreamProperties {
    protected Integer socketTimeout;
    protected Integer connectionTimeout;
    protected Integer maxTotalConnections;
    protected Integer defaultMaxConnectionsPerHost;
    protected Integer validateAfterInactivity;

    @Override
    public String toString() {
        return CommonUtils.getStrFromObj(this);
    }
}
