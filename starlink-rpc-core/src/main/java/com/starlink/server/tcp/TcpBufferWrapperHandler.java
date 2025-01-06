package com.starlink.server.tcp;

import com.starlink.codec.protocol.ProtocolConstant;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;

/**
 * TCP 消息处理器包装
 * 装饰者模式，使用 recordParser 对原有的 buffer 处理能力进行增强
 */
public class TcpBufferWrapperHandler implements Handler<Buffer> {

    /**
     * 解析器，用于解决半包、粘包问题
     */
    private final RecordParser recordParser;

    public TcpBufferWrapperHandler(Handler<Buffer> bufferHandler) {
        recordParser = initRecordParser(bufferHandler);
    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    /**
     * 初始化解析器
     */
    private RecordParser initRecordParser(Handler<Buffer> bufferHandler) {
        // 构造 parser
        RecordParser parser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH);

        parser.setOutput(new Handler<Buffer>() {
            // 初始化
            int bodyLength = -1;
            // 一个完整消息帧
            Buffer messageBuffer = Buffer.buffer();

            @Override
            public void handle(Buffer buffer) {
                // 1. 每次循环，首先读取消息头
                if (-1 == bodyLength) {
                    // 读取消息体长度
                    bodyLength = buffer.getInt(13);
                    parser.fixedSizeMode(bodyLength);
                    // 写入头信息到结果
                    messageBuffer.appendBuffer(buffer);
                } else {
                    // 2. 然后读取消息体
                    // 写入体信息到结果
                    messageBuffer.appendBuffer(buffer);
                    // 已拼接为完整 Buffer，执行处理
                    bufferHandler.handle(messageBuffer);
                    // 重置一轮
                    parser.fixedSizeMode(ProtocolConstant.MESSAGE_HEADER_LENGTH);
                    bodyLength = -1;
                    messageBuffer = Buffer.buffer();
                }
            }
        });

        return parser;
    }
}
