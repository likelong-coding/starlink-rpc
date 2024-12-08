package com.starlink.serializer;

import com.starlink.codec.serialization.Serializer;
import com.starlink.spi.SpiLoader;
import org.junit.Test;

/**
 * @author likelong
 * @date 2024/12/8 12:33
 * @description
 */
public class SpiLoaderTest {

    @Test
    public void test1(){
        System.out.println(SpiLoader.load(Serializer.class));
        Object test = SpiLoader.getInstance(Serializer.class, "json");
        System.out.println(test);
    }
}
