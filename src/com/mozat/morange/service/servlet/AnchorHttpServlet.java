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

import com.mozat.morange.dbcache.tables.LiveRoom;

import game.live.model.LiveRoomService;
import net.sf.json.JSONObject;
import netty.util.MathUtil;

/**
 * 主播开播停播(web端)
 * @author leen
 *
 */
public class AnchorHttpServlet extends HttpServlet{
	private static final long serialVersionUID = -2316396870124896816L;
	private static final Logger logger = LoggerFactory.getLogger(AnchorHttpServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String type = req.getParameter("type");
		int roomId = MathUtil.parseInt(req.getParameter("roomId"));
		LiveRoom liveRoom = LiveRoom.getOne(roomId);
		JSONObject json = new JSONObject();
		if(liveRoom == null){
			json.put("success", false);
			json.put("reason", "No such room("+roomId+")");
			response(resp, json.toString());
			return;
		}
		if("start".equals(type)){
			LiveRoomService.addLiveRoom(liveRoom);
		} else {
			LiveRoomService.anchorExit(liveRoom.ANCHOR);
		}
		json.put("success", true);
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
