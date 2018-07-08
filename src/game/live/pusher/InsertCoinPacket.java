package game.live.pusher;

import com.mozat.morange.dbcache.tables.AnchorMachine;

import game.live.model.LiveRoomService;
import game.live.qingmeng.QingMengService;
import game.packet.Packet;
import game.packet.PacketManager;
import netty.GameModels.UserMgr;
import protocols.header.packet.Builder;
import protocols.live.pusher.insertcoin;

public class InsertCoinPacket extends Packet {
	//请求
	private int roomId;
	
	//下发
	private int error;
	private String name;
	
	@Override
	public void execPacket(){
		int anchorId = LiveRoomService.getRoomAnchor(roomId);
		if(anchorId == -1){
			error = 1;
			errDesc = "房间已停播";
			PacketManager.send(userId, this);
			logger.info("User({}) insert coin failed because can't find room({})", userId, roomId);
			return;
		}
		AnchorMachine mac = AnchorMachine.getOneByCriteria(AnchorMachine.AttrANCHOR_ID.eq(anchorId),AnchorMachine.AttrIS_ONLINE.eq("1"));
		if(mac == null){
			error = 2;
			errDesc = "主播未链接推币机";
			PacketManager.send(userId, this);
			logger.info("User({}) insert coin failed because no machine online in room({}), anchorId({})", userId, roomId, anchorId);
			return;
		}
		int holder = PusherHolder.getInstance().getHolder(roomId);
		if(holder > 0 && holder != userId){
			error = 4;
			errDesc = "其他用户已占有，请5秒后重试";
			name = UserMgr.getInstance().getUserName(holder);
			PacketManager.send(userId, this);
			logger.info("User({}) insert coin failed because other({}) holding pusher.", userId, holder);
			return;
		}
		/*int cost = 100;
		int oldRoomCardCount = GameUserUidMgr.getInstance().getUserRoomCardCount(userId);
		if(oldRoomCardCount < cost){
			error = 3;
			errDesc = "金币不足，扣费失败";
			PacketManager.send(userId, this);
			logger.info("User("+userId+") send gift failed, because no enough room card. roomId:"+roomId+",curr:"+oldRoomCardCount+",cost:"+cost);
			return;
		}
		Date now = new Date();
		String userName = GameUserUidMgr.getInstance().getUserName(userId);
		int nowRoomCardCount = oldRoomCardCount - cost;
		GameUserUidMgr.getInstance().setUserRoomCardCount(userId, nowRoomCardCount);
		RoomCardBill bill = RoomCardBill.create(RoomCardBill.AttrUSER_ID.set(userId+""), 
				RoomCardBill.AttrUSER_NAME.set(userName),
				RoomCardBill.AttrSOURCE.set("live"),
				RoomCardBill.AttrSOURCE_ID.set(roomId+""),
				RoomCardBill.AttrSOURCE_NAME.set("直播房间("+roomId+")内投币"),
				RoomCardBill.AttrAMOUNT.set(-cost),
				RoomCardBill.AttrBEFORE_BAL.set(oldRoomCardCount),
				RoomCardBill.AttrAFTER_BAL.set(nowRoomCardCount),
				RoomCardBill.AttrCREATE_TIME.set(now));*/
		QingMengService.handle("insertcoin", anchorId, mac.MACHINE_MAC);
		error = 0;
		PusherHolder.getInstance().addHolder(roomId, userId);
		PacketManager.send(userId, this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		insertcoin.request request = insertcoin.request.parseFrom(bytes);
		roomId = request.getRoomId();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		insertcoin.response.Builder builder = insertcoin.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setName(name);
		
		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
