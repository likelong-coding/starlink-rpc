package com.starlink.server;

import com.starlink.spi.SpiLoader;

public class VertxServerFactory {
    static {
        SpiLoader.load(VertxServer.class);
    }

    public static VertxServer getInstance(String key) {
        return SpiLoader.getInstance(VertxServer.class, key);
    }
}
