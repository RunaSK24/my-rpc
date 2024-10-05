package cn.edu.szu.myrpc.registry;

import cn.edu.szu.myrpc.config.RegistryConfig;
import cn.edu.szu.myrpc.model.ServiceMetaInfo;

import java.util.List;

public interface Registry {
    /**
     * 初始化
     * @param registryConfig 注册中心配置
     */
    void init(RegistryConfig registryConfig);

    /**
     * 注册服务（服务端）
     * @param serviceMetaInfo 服务注册信息
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 心跳检测（服务端）
     */
    void heartBeat();

    /**
     * 注销服务（服务端）
     * @param serviceMetaInfo 服务注册信息
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 服务发现（获取某服务的所有节点，消费端）
     * @param serviceKey 服务键名
     * @return 符合要求的服务列表
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 服务销毁
     */
    void destroy();
}
