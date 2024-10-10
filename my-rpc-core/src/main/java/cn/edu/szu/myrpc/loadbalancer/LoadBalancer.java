package cn.edu.szu.myrpc.loadbalancer;

import cn.edu.szu.myrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡接口
 */
public interface LoadBalancer {

    /**
     * 根据负载均衡策略选择调用的服务
     * @param requestParams 请求参数
     * @param serviceMetaInfoList 服务列表
     * @return 选中的服务
     */
    ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);

}
