package com.zer.redis.study;

import com.google.gson.Gson;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @author hongxf
 * @create 2016-12-08 10:01
 */
public class Chapter05 {

    @Test
    public void getCityOfIp() {
        Jedis conn = new Jedis("192.168.11.81");
        conn.select(0);
        String ipAddress = "116.231.231.240";
        int score = ipToScore(ipAddress);
        Set<String> results = conn.zrevrangeByScore("ip2cityid:", score, 0, 0, 1);
        if (results.size() == 0) {
            return;
        }

        String cityId = results.iterator().next();
        cityId = cityId.substring(0, cityId.indexOf('_'));
        String[] json = new Gson().fromJson(conn.hget("cityid2city:", cityId), String[].class);
        for (String str : json) {
            System.out.println(str);
        }
    }

    private int ipToScore(String ipAddress) {
        int score = 0;
        for (String v : ipAddress.split("\\.")){
            score = score * 256 + Integer.parseInt(v, 10);
        }
        return score;
    }
}
