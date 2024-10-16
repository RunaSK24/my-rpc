package cn.edu.szu.myrpc.bootstrap;

import cn.edu.szu.myrpc.RpcApplication;

/**
 * 服务消费者启动类
 */
public class ConsumerBootstrap {

    /**
     * 初始化
     */
    public static void init() {
        RpcApplication.getRpcConfig();
    }

}
