package cn.edu.szu.myrpc.server;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer {
    @Override
    public void doStart(int port) {
        Vertx vertx = Vertx.vertx();

        // 创建HTTP服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        server.requestHandler(request -> {
            // 打印接收请求
            System.out.println("Received request: " + request.method() + " " + request.uri());

            request.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x HTTP server!");
        });

        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("Server is now listening on port " + port);
            } else {
                System.out.println("Failed to start server: " + result.cause());
            }
        });
    }
}
