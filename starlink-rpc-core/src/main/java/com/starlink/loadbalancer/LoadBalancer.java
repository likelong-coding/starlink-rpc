package com.starlink.loadbalancer;


import com.starlink.model.ServiceMetaInfo;

import java.util.List;

/**
 * 负载均衡器（消费端使用）
 */
public interface LoadBalancer {

    /**
     * 选择服务调用
     * @param serviceMetaInfoList 可用服务列表
     */
    ServiceMetaInfo select(List<ServiceMetaInfo> serviceMetaInfoList);

}
