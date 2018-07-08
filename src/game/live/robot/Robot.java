package game.live.robot;

import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.dbcache.tables.Anchor;
import com.mozat.morange.dbcache.tables.LiveGift;

import game.common.CommonConfig;
import game.live.model.LiveGiftMgr;
import game.live.model.LiveRoomModel;
import game.live.model.LiveRoomService;
import game.packet.PacketManager;
import game.packet.PacketTypes;
import netty.util.RandomUtil;
import protocols.header;
import protocols.live.common.user_info;
import protocols.live.sendgift.gift_notify;
import protocols.live.sendmsg.msg_notify;

public class Robot {
	
	private static Logger logger = LoggerFactory.getLogger(Robot.class);
	
	private Random rand = new Random();
	private Timer timer = new Timer();
	
	private LiveRoomModel model;
	private String name;
	
	public Robot(String name, LiveRoomModel model){
		this.name = name;
		this.model = model;
	}
	
	public void takeAction(){
		int minIvl = CommonConfig.getInt(CommonConfig.ROBOT_ACTION_MIN_INTERVAL, 10);
		int maxIvl = CommonConfig.getInt(CommonConfig.ROBOT_ACTION_MAX_INTERVAL, 60);
		long delay = (minIvl + rand.nextInt(maxIvl-minIvl))*1000;
		timer.schedule(new MyTask(), delay);
		logger.info(this+" will take action after "+delay+" seconds");
	}
	
	public void stop(){
		timer.cancel();
		logger.info(this+" stops.");
	}
	
	private class MyTask extends TimerTask {
		@Override
		public void run() {
			sendGiftOrMsg();
			int minIvl = CommonConfig.getInt(CommonConfig.ROBOT_ACTION_MIN_INTERVAL, 5);
			int maxIvl = CommonConfig.getInt(CommonConfig.ROBOT_ACTION_MAX_INTERVAL, 30);
			long delay = (minIvl + rand.nextInt(maxIvl-minIvl))*1000;
			timer.schedule(new MyTask(), delay);
		}
	}

	public void sendGiftOrMsg(){
		int giftPercent = CommonConfig.getPercent(CommonConfig.ROBOT_SEND_GIFT_PERCENT, 5);
		boolean isGift = RandomUtil.randomBoolean(giftPercent, 100-giftPercent);
		send(isGift);
	}
	
	public void sendMsg(){
		send(false);
	}
	
	public void sendGift(){
		send(true);
	}
	
	private void send(boolean isGift){
		if(model == null){
			return;
		}
		boolean crossRoom = false;
		
		user_info.Builder userInfo = user_info.newBuilder();
		userInfo.setRank(rand.nextInt(5));
		userInfo.setRankName("");
		userInfo.setUserId(687);
		userInfo.setUserName(name);
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setVersion(1);
    	head.setSubversion(0);
		if(isGift){
			Anchor anchor = Anchor.getOne(model.getConfig().ANCHOR);
			if(anchor == null){
				return;
			}
			LiveGift gift = LiveGiftMgr.randomGift();
			anchor.HISTORY_CUBE = anchor.HISTORY_CUBE + gift.COST;
			anchor.update();
			gift_notify.Builder notify = gift_notify.newBuilder();
			notify.setSender(userInfo.build());
			notify.setGiftCount(1);
			notify.setGiftId(gift.ID);
			notify.setBingo(false);
			notify.setHistoryCube(anchor.HISTORY_CUBE);
			logger.debug(this+" send gift("+gift.GIFT_NAME+") to anchor.");
			head.setCommand(PacketTypes.JOIN_NOTIFY);
			head.setBody(notify.buildPartial().toByteString());
		} else {
			String msg = LiveWordMgr.saySomething();
			model.addMsgCount();
			msg_notify.Builder notify = msg_notify.newBuilder();
			notify.setSender(userInfo.build());
			notify.setMsg(name+":"+msg);
			notify.setMsgCount(model.getMsgCount());
			logger.debug(this+" send msg("+msg+") to anchor.");
			head.setCommand(PacketTypes.MSG_NOTIFY);
			head.setBody(notify.buildPartial().toByteString());
		}
		
    	Set<Integer> users = crossRoom?LiveRoomService.getPlatformUser():model.getRoomUser();
		for(Integer other : users){
			PacketManager.send(other, head.buildPartial().toByteArray());
		}
	}
}
