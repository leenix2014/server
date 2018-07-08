package com.mozat.morange.service.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.dbcache.tables.Anchor;

import net.sf.json.JSONObject;
import netty.util.MathUtil;
import netty.util.StringUtil;

/**
 * 检查主播是否存在
 * @author leen
 *
 */
@MultipartConfig
public class ExistsAnchorHttpServlet extends HttpServlet{
	private static final long serialVersionUID = -2316396870124896816L;
	private static final Logger logger = LoggerFactory.getLogger(ExistsAnchorHttpServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String str = req.getParameter("userId");
		if(StringUtil.isEmpty(str)){
			Part part = req.getPart("userId");
			if(part != null){
				BufferedReader br=new BufferedReader(new InputStreamReader(part.getInputStream()));
				str=br.readLine();//读取请求参数值
			}
		}
		logger.info("request existsAnchor({})", str);
		int anchorId = MathUtil.parseInt(str);
		Anchor anchor = Anchor.getOne(anchorId);
		JSONObject result = new JSONObject();
		JSONObject json = new JSONObject();
		json.put("exists", anchor != null);
		result.put("retcode", 0);
		result.put("result", json);
		
		response(resp, result.toString());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}


	private void response(HttpServletResponse resp, String content) throws IOException {
		logger.info("response:result=" + content);
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(), "UTF-8"));
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		writer.append(content);
		writer.flush();
		writer.close();
	}
}
