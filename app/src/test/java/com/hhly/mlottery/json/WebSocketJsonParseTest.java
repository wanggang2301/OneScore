package com.hhly.mlottery.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hhly.mlottery.bean.websocket.WebSocketCPIResult;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * 描    述：
 * 作    者：longs@13322.com
 * 时    间：2016/6/23.
 */
public class WebSocketJsonParseTest {

    @Test
    public void parseWebSocketCPIResultTest() throws Exception {
        String jsonString = "{'data':{'keepTime':49,'statusOrigin':3},'thirdId':'338827','type':1}";

        WebSocketCPIResult<WebSocketCPIResult.UpdateTimeAndStatus> result = WebSocketCPIResult.getTimeAndStatusFromJson(jsonString);

        Assert.assertEquals(49, result.getData().getKeepTime());
    }

    @Test
    public void parseWebSocketListResultTest() throws Exception {
        String jsonString = "{\"data\":[{\"comId\":\"3\",\"leftOdds\":\"3.00\",\"mediumOdds\":\"3.20\",\"oddType\":\"2\",\"rightOdds\":\"2.30\",\"uptime\":\"14:43\"}],\"thirdId\":\"340250\",\"type\":2}";
        JSONObject jsonObject = JSON.parseObject(jsonString);
        Assert.assertEquals(2, jsonObject.get("type"));
//        JSON.parseObject(jsonString, WebSocketCPIResult.class);

        WebSocketCPIResult<List<WebSocketCPIResult.UpdateOdds>> result =
                WebSocketCPIResult.getOddsFromJson(jsonString);

        List<WebSocketCPIResult.UpdateOdds> data = result.getData();
        for (WebSocketCPIResult.UpdateOdds odds : data) {
            System.out.println(odds.getComId());
        }
        Assert.assertEquals("3", data.get(0).getComId());
    }
}
