package game.loginReward.packet;

import java.util.Date;
import java.util.Set;

import com.mozat.morange.dbcache.tables.RoomCardBill;

import game.loginReward.LoginRecord;
import game.loginReward.LoginRewardConfig;
import game.packet.Packet;
import game.packet.PacketManager;
import netty.GameModels.UserMgr;
import protocols.header.packet.Builder;
import protocols.loginreward.getloginprize;

public class GetLoginPrizePacket extends Packet{
	//请求
	getloginprize.request request;
	
	//下发
	public int error;
	private int day;
	private int roomCard;

	@Override
	public void execPacket(){
		int day = request.getDay();
		LoginRecord loginRecord = LoginRecord.load(userId);
		if (loginRecord == null) {
			logger.info("Load LoginRecord is null,userId=" + userId + ",day=" + day);
			GetLoginPrizePacket packet = new GetLoginPrizePacket();
			packet.error = 1;
			packet.errDesc = "未有登录记录";
			PacketManager.send(userId, packet);
			return;
		}
		
		LoginRewardConfig config = LoginRewardConfig.getLoginRewardConfig(day);
		if(config == null) {
			logger.info("Login Config is null,userId=" + userId + ",day=" + day);
			GetLoginPrizePacket packet = new GetLoginPrizePacket();
			packet.error = 1;
			packet.errDesc = "服务器未有登录奖励配置，请联系运营人员";
			PacketManager.send(userId, packet);
			return;
		}
		
		int loginDay = loginRecord.getLoginDay();
		if (day > loginDay) {//不可领取
			
			GetLoginPrizePacket packet = new GetLoginPrizePacket();
			packet.error = 1;
			packet.errDesc = "尚未到时间领取";
			PacketManager.send(userId, packet);
			
			return;
		}
		
		Set<Integer> gotDays = loginRecord.getGotDays();
		if (gotDays.contains(day)) {//已领取
			logger.info("User({}) already get day({}) prize.", userId, request.getDay());
			GetLoginPrizePacket packet = new GetLoginPrizePacket();
			packet.error = 1;
			packet.errDesc = "已领取过了";
			PacketManager.send(userId, packet);
			return;
		}
		
		//给奖励
		int roomCard = config.getRoomCard();
		int oldRoomCardCount = UserMgr.getInstance().getCuber(userId);
		int nowRoomCardCount = oldRoomCardCount + roomCard;
		UserMgr.getInstance().setCuber(userId, nowRoomCardCount);
		
		gotDays.add(day);
		loginRecord.setGotDays(gotDays);
		loginRecord.update();
		
		RoomCardBill.create(RoomCardBill.AttrUSER_ID.set(userId+""), 
				RoomCardBill.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(userId)),
				RoomCardBill.AttrSOURCE.set("signin"),
				RoomCardBill.AttrSOURCE_ID.set(day+""),
				RoomCardBill.AttrSOURCE_NAME.set("连续第"+day+"天登录"),
				RoomCardBill.AttrAMOUNT.set(roomCard),
				RoomCardBill.AttrBEFORE_BAL.set(oldRoomCardCount),
				RoomCardBill.AttrAFTER_BAL.set(nowRoomCardCount),
				RoomCardBill.AttrCREATE_TIME.set(new Date()));
		
		GetLoginPrizePacket packet = new GetLoginPrizePacket();
		packet.error = 0;
		packet.setDay(day);
		packet.setRoomCard(roomCard);
		PacketManager.send(userId, packet);
		logger.info("User({}) get day({}) prize, reward room card:{}", userId, request.getDay(), roomCard);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		request = getloginprize.request.parseFrom(bytes);
		logger.info("User({}) request get login prize, day:{}", userId, request.getDay());
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		getloginprize.response.Builder builder = getloginprize.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setDay(this.day);
		builder.setRoomCard(this.roomCard);
		pktBuilder.setBody(builder.buildPartial().toByteString());					
	}
	
	public void setDay(int value){
		this.day = value;
	}
	
	public void setRoomCard(int value){
		this.roomCard = value;
	}
}
