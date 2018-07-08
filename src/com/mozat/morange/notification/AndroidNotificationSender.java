package com.mozat.morange.notification;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.game.Global;
import com.mozat.morange.log.TraceLog;

public class AndroidNotificationSender implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(AndroidNotificationSender.class);
    private final static ConcurrentLinkedQueue<JSONObject> queue = new ConcurrentLinkedQueue<JSONObject>();

    static void push(JSONObject gcmData) {
        queue.add(gcmData);
        /* too many to send, remove the oldest */
        if (queue.size() > 200) {
            queue.poll();
        }
    }

    @Override
    public void run() {
        /* send all or send half */
        int toSend = queue.size() > 20 ? queue.size() / 2 : queue.size();
        TraceLog.info("[AndroidNotificationSender.run], queueSize=" + queue.size() + ",toSend=" + toSend);
        if (toSend == 0) {
            return;
        }
        URL url;
        try {
            url = new URL("https://android.googleapis.com/gcm/send");
        } catch (MalformedURLException e) {
            logger.error("", e);
            return;
        }
        for (int i = 0; i < toSend; i++) {
            JSONObject jGcmData = queue.poll();
            if (jGcmData == null) {
                break;
            }
            // What to send in GCM message.
            String apiKey = Global.getGcmAndroidApiKey();
            if (apiKey.isEmpty()) {
                TraceLog.info("[AndroidNotificationSender]run, Google API_KEY is empty");
                return;
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Authorization", "key=" + apiKey);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                // Send GCM message content.
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(jGcmData.toString().getBytes());
                outputStream.close();

                // Read GCM response.
                InputStream inputStream = conn.getInputStream();
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                String resp = result.toString("UTF-8");
                inputStream.close();

                if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                    JSONObject response = new JSONObject(resp);
                    int success = response.optInt("success", 0);
                    if (success > 0) {
                        continue;
                    }
                    JSONArray results = response.optJSONArray("results");
                    if (results == null) {
                        continue;
                    }
                    for (int j = 0, max = results.length(); j < max; j++) {
                        JSONObject temp = results.getJSONObject(j);
                        if (response.has("error")) {
                            TraceLog.info("[AndroidNotificationSender]run, send failed, error=" + temp.getString("error"));
                            break;
                        }
                    }
                } else {
                    TraceLog.info("[AndroidNotificationSender]run, http error, responseCode=" + conn.getResponseCode());
                }
            } catch (Exception e) {
                logger.error("", e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }
}
