package com.mozat.morange.game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * 字符串转化为json ，接收了异常处理
     *
     * @param str 需要进行转换为json的字符串
     */
    public static JSONObject strToJson(String str) {
        try {
            return new JSONObject(str);
        } catch (JSONException e) {
            logger.error("", e);
            return new JSONObject();
        }
    }

    //TODO ?
    public static JSONObject strToJson1(String str) throws JSONException {
        if (str == null) {
            throw new JSONException("data format jsonObject is wrong!");
        }

        if (str.trim().isEmpty()) {
            return new JSONObject();
        }

        return new JSONObject(str);
    }

    //TODO ?
    public static JSONArray strToJsonArray(String str) {
        try {
            return new JSONArray(str);
        } catch (JSONException e) {
            logger.error("", e);
            return new JSONArray();
        }
    }

    //TODO ?
    public static JSONArray strToJsonArray1(String str) throws JSONException {
        if (str == null) {
            throw new JSONException("data format jsonarray is wrong!");
        }

        if (str.trim().isEmpty()) {
            return new JSONArray();
        }

        return new JSONArray(str);
    }
}
