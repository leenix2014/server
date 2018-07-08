package com.mozat.morange.service.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.log.TraceLog;

import game.coin.MyCoinPacket;
import game.packet.PacketManager;
import game.user.Users;
import net.sf.json.JSONObject;
import netty.GameModels.UserMgr;
import netty.util.MathUtil;

/**
 * 代理充值刷新内存和通知用户
 * @author leen
 *
 */
public class AgentHttpServlet extends HttpServlet{
	private static final long serialVersionUID = 8723543842587556828L;
	private static final Logger logger = LoggerFactory.getLogger("AgentHttpServlet");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int userId = MathUtil.parseInt(req.getParameter("userId"));
		String type = req.getParameter("type");
		TraceLog.info("AgentHttpServlet.doGet userId=" + userId);
		Users.loadFromDb(userId);
		if("coin".equals(type)){
			MyCoinPacket packet = new MyCoinPacket();
			packet.coinCount = UserMgr.getInstance().getUserCoin(userId);
			packet.withdrawCount = MathUtil.parseInt(req.getParameter("amount"));				
			PacketManager.send(userId, packet);	
		} else {
			UserMgr.getInstance().sendCuber(userId, 2);
		}
		JSONObject retJson = new JSONObject();
		retJson.put("r", true);
		TraceLog.info(userId, "AndroidPushServlet doGet", "ret:" + retJson.toString());
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
