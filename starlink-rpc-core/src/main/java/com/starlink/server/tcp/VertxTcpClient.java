package com.starlink.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.starlink.RpcApplication;
import com.starlink.codec.protocol.*;
import com.starlink.model.RpcRequest;
import com.starlink.model.RpcResponse;
import com.starlink.model.ServiceMetaInfo;
import com.starlink.server.client.VertxClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class VertxTcpClient implements VertxClient {

    @Override
    public RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws Exception {
        // 发送 TCP 请求
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();
        netClient.connect(serviceMetaInfo.getServicePort(), serviceMetaInfo.getServiceHost(),
                result -> {
                    if (result.succeeded()) {
                        log.info("Connected to TCP server");
                        io.vertx.core.net.NetSocket socket = result.result();
                        // 构造消息，发送数据
                        ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
                        ProtocolMessage.Header header = new ProtocolMessage.Header();
                        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
                        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
                        header.setSerializer((byte) ProtocolMessageSerializerEnum.getEnumByValue(RpcApplication.getRpcConfig().getSerializer()).getKey());
                        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                        header.setRequestId(IdUtil.getSnowflakeNextId());
                        protocolMessage.setHeader(header);
                        protocolMessage.setBody(rpcRequest);
                        // 编码请求
                        try {
                            Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
                            socket.write(encodeBuffer);
                        } catch (IOException e) {
                            throw new RuntimeException("协议消息编码错误");
                        }

                        // 接收响应
                        socket.handler(buffer -> {
                            try {
                                ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                                responseFuture.complete(rpcResponseProtocolMessage.getBody());
                            } catch (IOException e) {
                                throw new RuntimeException("协议消息解码错误");
                            }
                        });
                    } else {
                        log.error("Failed to connect to TCP server");
                    }
                });

        RpcResponse rpcResponse = responseFuture.get();
        // 关闭连接
        netClient.close();
        return rpcResponse;
    }
}
