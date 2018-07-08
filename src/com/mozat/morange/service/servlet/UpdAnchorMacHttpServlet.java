package com.mozat.morange.service.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.dbcache.tables.Anchor;
import com.mozat.morange.dbcache.tables.AnchorMachine;

import net.sf.json.JSONObject;
import netty.util.MathUtil;
import netty.util.StringUtil;

/**
 * 更新主播青萌机器状态
 * @author leen
 *
 */
@MultipartConfig
public class UpdAnchorMacHttpServlet extends HttpServlet{
	private static final long serialVersionUID = -2316396870124896816L;
	private static final Logger logger = LoggerFactory.getLogger(UpdAnchorMacHttpServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String mac = req.getParameter("machine_MAC");
		int anchorId = MathUtil.parseInt(req.getParameter("platform_userid"));
		String online = req.getParameter("is_online");
		String supplies = req.getParameter("supplies");
		String binding = req.getParameter("is_binding");
		String status = req.getParameter("status");
		logger.info("request updateMachine({},{},{},{},{},{})", mac, anchorId, online, supplies, binding, status);
		AnchorMachine machine = AnchorMachine.getOneByCriteria(AnchorMachine.AttrMACHINE_MAC.eq(mac),
				AnchorMachine.AttrANCHOR_ID.eq(anchorId));
		Date now = new Date();
		if(machine == null){
			AnchorMachine.create(anchorId, AnchorMachine.AttrCREATE_TIME.set(now),
					AnchorMachine.AttrIS_BINDING.set(binding),
					AnchorMachine.AttrIS_ONLINE.set(online),
					AnchorMachine.AttrMACHINE_MAC.set(mac),
					AnchorMachine.AttrSTATUS.set(status),
					AnchorMachine.AttrSUPPLIES.set(supplies),
					AnchorMachine.AttrUPDATE_TIME.set(now));
		} else {
			machine.IS_BINDING = StringUtil.nonNull(binding);
			machine.IS_ONLINE = StringUtil.nonNull(online);
			machine.STATUS = StringUtil.nonNull(status);
			machine.SUPPLIES = StringUtil.nonNull(supplies);
			machine.UPDATE_TIME = now;
			machine.update();
		}
		
		JSONObject json = new JSONObject();
    	json.put("state", 0);
    	json.put("content", new JSONObject());
		response(resp, json.toString());
		
		Anchor anchor = Anchor.getOne(anchorId);
		if(anchor == null || "live".equals(anchor.ANCHOR_TYPE)){
			return;
		}
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
