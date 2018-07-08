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

import com.mozat.morange.service.BusinessService;

import game.baccarat.model.BaccaratRoomService;
import game.coinroom.model.CoinRoomService;
import game.common.CommonConfig;
import game.live.model.LiveGiftMgr;
import game.live.robot.LiveNameMgr;
import game.live.robot.LiveWordMgr;
import game.loginReward.LoginRewardService;
import game.lottery.PrizeService;
import game.roulette.model.RouletteRoomService;
import game.task.TaskService;
import net.sf.json.JSONObject;

/**
 * 刷新服务器配置
 * @author leen
 *
 */
public class RefreshHttpServlet extends HttpServlet{
	private static final long serialVersionUID = -582199712956896186L;
	private static final Logger logger = LoggerFactory.getLogger(RefreshHttpServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String type = req.getParameter("type");
		logger.info("Refresh request accepted, type="+type);
		boolean success = true;
		if("all".equals(type)){
			BusinessService.init();
		} else if("common".equals(type)){
			CommonConfig.init();
		} else if("login".equals(type)){
			LoginRewardService.init();
		} else if("task".equals(type)){
			TaskService.init();
		} else if("prize".equals(type)){
			PrizeService.init();
		} else if("livegift".equals(type)){
			LiveGiftMgr.init();
		} else if("liveword".equals(type)){
			LiveWordMgr.init();
		} else if("livename".equals(type)){
			LiveNameMgr.init();
		} else if("coinroom".equals(type)){
			CoinRoomService.init();
		} else if("roulette".equals(type)) {
			RouletteRoomService.init();
		} else if("baccarat".equals(type)) {
			BaccaratRoomService.init();
		} else {
			success = false;
		}
		JSONObject json = new JSONObject();
		json.put("success", success);
		if(!success){
			json.put("reason", "No such type:"+type);
		}
		response(resp, json.toString());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}


	private void response(HttpServletResponse resp, String content) throws IOException {
		logger.info("response:result=" + content);
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(resp.getOutputStream(), "UTF-8"));
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/plain");
		writer.append(content);
		writer.flush();
		writer.close();
	}
}
