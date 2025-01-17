package com.starlink.loadbalancer;

import com.starlink.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 为了兼顾无需请求参数的负载均衡
 * 使用抽象类
 */
public abstract class AbstractLoadBalancer implements LoadBalancer {

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        return select(serviceMetaInfoList);
    }

    protected abstract ServiceMetaInfo select(List<ServiceMetaInfo> serviceMetaInfoList);
}
