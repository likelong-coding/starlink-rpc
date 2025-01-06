package com.starlink.server.client;

import com.starlink.spi.SpiLoader;

public class VertxClientFactory {
    static {
        SpiLoader.load(VertxClient.class);
    }

    public static VertxClient getInstance(String key) {
        return SpiLoader.getInstance(VertxClient.class, key);
    }
}
