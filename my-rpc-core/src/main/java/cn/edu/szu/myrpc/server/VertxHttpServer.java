package cn.edu.szu.myrpc.server;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer {
    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();

        // 创建HTTP服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        server.requestHandler(new HttpServerHandler());

        server.listen(port, result -> {
            // 启动监听日志
            if (result.succeeded()) {
                System.out.println("Server is now listening on port " + port);
            } else {
                System.out.println("Failed to start server: " + result.cause());
            }
        });
    }
}
