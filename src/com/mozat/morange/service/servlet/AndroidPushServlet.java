package com.mozat.morange.service.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.log.TraceLog;
import com.mozat.morange.notification.NotificationService;

public class AndroidPushServlet extends HttpServlet{
	private static final long serialVersionUID = 8723543842587556827L;
	private static final Logger logger = LoggerFactory.getLogger("androidpush");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int monetId = Integer.parseInt(req.getParameter("monetId"));
		JSONObject retJson = NotificationService.getAndroidPushJson(monetId);
		TraceLog.info(monetId, "AndroidPushServlet doGet", "ret:" + retJson.toString());
		success(resp, retJson.toString());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}


	private void success(HttpServletResponse resp, String content) throws IOException {
		logger.info("response:result=" + content);
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(), "UTF-8"));
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain");
		writer.append(content);
		writer.flush();
		writer.close();
	}
}
