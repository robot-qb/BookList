package com.casper.testdrivendevelopment.test;

import com.casper.testdrivendevelopment.data.ShopLoader;
import com.casper.testdrivendevelopment.data.model.Shop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShopLoaderTest {

    ShopLoader shopLoader;
    @Before
    public void setUp() throws Exception {
        shopLoader=new ShopLoader();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getShops() {
        //测试新创建的是非空但包含0个元素
        assertNotNull(shopLoader.getShops());
        assertEquals(0,shopLoader.getShops().size());
    }

    @Test
    public void download() {
        //这个函数测试是否能正确下载数据
        String content=shopLoader.download("http://file.nidama.net/class/mobile_develop/data/bookstore.json");
        //验证下载的内容长度超过300
        assertTrue(content.length()>300);
        //验证下载的内容中包含特定的字符串
        assertTrue(content.contains("\"longitude\": \"113.526421\""));
    }

    @Test
    public void parseJson() {

        String content="{"
                + "  \"shops\": [{"
                + "    \"name\": \"暨南大学珠海校区\","
                + "    \"latitude\": \"22.255925\","
                + "    \"longitude\": \"113.541112\","
                + "    \"memo\": \"暨南大学珠海校区\""
                + "  },"
                + "    {"
                + "      \"name\": \"明珠商业广场\","
                + "      \"latitude\": \"22.251953\","
                + "      \"longitude\": \"113.526421\","
                + "      \"memo\": \"珠海二城广场\""
                + "    }"
                + "  ]"
                + "}";
        shopLoader.parseJson(content);
        assertEquals(2,shopLoader.getShops().size());
        Shop shop=shopLoader.getShops().get(1);
        assertEquals("明珠商业广场",shop.getName());
        assertEquals("珠海二城广场",shop.getMemo());
        assertEquals(22.2519523,shop.getLatitude(),1e-6);
        assertEquals(113.526421,shop.getLongitude(),1e-6);
    }
}