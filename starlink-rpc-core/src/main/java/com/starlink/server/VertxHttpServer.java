package com.starlink.server;

import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Vertx HTTP 服务器
 */
public class VertxHttpServer implements HttpServer {

    private static final Logger logger = LoggerFactory.getLogger(VertxHttpServer.class);

    /**
     * 启动服务器
     *
     * @param port 端口
     */
    public void doStart(int port) {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建 HTTP 服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // 处理请求
        server.requestHandler(new HttpServerHandler());

        // 启动 HTTP 服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                logger.info("Server is now listening on port " + port);
            } else {
                logger.error("Failed to start server: " + result.cause());
            }
        });
    }
}
