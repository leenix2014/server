package com.mozat.morange.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Random;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import netty.util.StringUtil;

public class HttpUtil
{
	private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	private static int timeOut = 10 * 1000;
	
	public static String postHttps(String url, HttpEntity reqEntity) {
        CloseableHttpClient httpclient = createSSLClientDefault();
        CloseableHttpResponse response = null;
        String res = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(timeOut)
            		.setConnectTimeout(timeOut).setSocketTimeout(timeOut)
            		.setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();
            httpPost.setEntity(reqEntity);
            httpPost.setConfig(config);
            logger.debug("Executing request " + httpPost.getRequestLine());
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            logger.debug("status:"+response.getStatusLine());
            res = EntityUtils.toString(entity);
            logger.debug("response:"+res);
            EntityUtils.consume(entity);
        } catch (Exception e) {
			logger.error("postHttps error!url="+url, e);
		} finally {
            try {
				if(response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return res;
    }
	
	public static CloseableHttpClient createSSLClientDefault(){
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				//信任所有
		        @Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		             return true;
		        }
		     }).build();
		     SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
		     return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		 } catch (Exception e) {
		     logger.error("createSSLClientDefault failed!", e);
		 }
		 return  HttpClients.createDefault();
	}
	
	public static JSONObject getJSONObjectFromURL(String url) {
		try {
			String result = HttpUtil.doPost(url, "");
			JSONObject obj = new JSONObject(result);
			return obj;
		} catch (Exception e) {
			logger.error("getJSONObjectFromURL:"+url+" error!", e);
			return null;
		}
	}
	
	public static String doGet(String link, String param){
		return request(link, param, "GET");
	}
	
	public static String doPost(String link, String param){
		return request(link, param, "POST");
	}
	
	public static String request(String link, String param, String method){
		StringBuilder sb = new StringBuilder();
		HttpURLConnection conn = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		try
		{
			URL url = new URL(link);
			conn = (HttpURLConnection) url.openConnection();
			if("POST".equals(method)){
				conn.setDoInput(true);
				conn.setDoOutput(true);
			}
			conn.setRequestMethod(method);
			conn.setConnectTimeout(timeOut);
			if("POST".equals(method)){
				pw = new PrintWriter(conn.getOutputStream());
				pw.write(param);
				pw.flush();
			}
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
			String data;
			while ((data = br.readLine()) != null)
			{
				sb.append(data);
			}
		} catch (Exception e)
		{
			logger.error( String.format("[doPost] link : %s params : %s", link,param));
			logger.error("[doPost] exception : " + e );
			e.printStackTrace();
		} finally  {
			try{
				if (pw!=null)
					pw.close();
				if (br!=null)
					br.close();
				if (conn!=null)
					conn.disconnect();
			} catch (Exception e) {
				logger.error("[doPost] exception : " + e );
			}
		}
		return sb.toString();
	}
	
	public static void main(String[] args){
		StringBuffer sb = new StringBuffer("http://op.qmeng.me/index.php?m=Api&c=User&a=handle&platform_id=1496380138&commend=14");
		sb.append("&platform_userid=").append(701);
		sb.append("&machine_MAC=").append("28f366c0701a");
		sb.append("&flag_code=").append(StringUtil.toSize(new Random().nextInt(10000)+"", 4, '0'));
		System.out.println(HttpUtil.doGet(sb.toString(), ""));
	}
}
