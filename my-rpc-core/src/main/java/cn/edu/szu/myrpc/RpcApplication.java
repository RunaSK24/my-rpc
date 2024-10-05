package cn.edu.szu.myrpc;

import cn.edu.szu.myrpc.config.RegistryConfig;
import cn.edu.szu.myrpc.config.RpcConfig;
import cn.edu.szu.myrpc.constant.RpcConstant;
import cn.edu.szu.myrpc.registry.Registry;
import cn.edu.szu.myrpc.registry.RegistryFactory;
import cn.edu.szu.myrpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {
    private static volatile RpcConfig rpcConfig;

    /**
     * 注册中心初始化、配置对象初始化，支持直接传入配置对象
     * @param newRpcConfig 配置对象
     */
    public static void init(RpcConfig newRpcConfig) {
        // 配置文件初始化
        rpcConfig = newRpcConfig;
        log.info("RPC init, config = {}", newRpcConfig.toString());

        // 注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init, config = {}", registryConfig);
        // 创建并注册 Shutdown Hook，JVM 退出时执行操作（清除已注册节点）
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

    /**
     * 读取配置文件初始化
     */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取配置（懒加载单例模式）
     * @return RPC配置对象
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
