package com.starlink.serializer;

import com.starlink.codec.serialization.Serializer;

import java.io.IOException;

/**
 * @author likelong
 * @date 2024/12/8 12:30
 * @description
 */
public class TestSerializer implements Serializer{
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> tClass) throws IOException {
        return null;
    }
}
