package cn.edu.szu.myrpc.server;

/**
 * HTTP 服务器接口
 */
public interface Server {
    /**
     * 启动服务器
     * @param port 端口号
     */
    void doStart(int port);
}
