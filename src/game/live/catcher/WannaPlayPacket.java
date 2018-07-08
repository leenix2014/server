package game.live.catcher;

import com.mozat.morange.dbcache.tables.AnchorMachine;

import game.live.model.LiveRoomService;
import game.live.qingmeng.QingMengService;
import game.packet.Packet;
import game.packet.PacketManager;
import netty.GameModels.UserMgr;
import protocols.header.packet.Builder;
import protocols.live.catcher.wannaplay;

public class WannaPlayPacket extends Packet {
	//请求
	private int roomId;
	private int count;
	
	//下发
	private int error;
	private String name = "";
	private int otherRemain;
	
	@Override
	public void execPacket(){
		int anchorId = LiveRoomService.getRoomAnchor(roomId);
		if(anchorId == -1){
			error = 1;
			errDesc = "房间已停播";
			PacketManager.send(userId, this);
			logger.info("User({}) get catcher failed because can't find room({})", userId, roomId);
			return;
		}
		AnchorMachine mac = AnchorMachine.getOneByCriteria(AnchorMachine.AttrANCHOR_ID.eq(anchorId),AnchorMachine.AttrIS_ONLINE.eq("1"));
		if(mac == null){
			error = 2;
			errDesc = "主播未链接娃娃机";
			PacketManager.send(userId, this);
			logger.info("User({}) get catcher failed because no machine online in room({}), anchorId:{}", userId, roomId, anchorId);
			return;
		}
		int holder = CatcherModel.getInstance().getHolder(roomId);
		name = UserMgr.getInstance().getUserName(holder);
		otherRemain = CatcherModel.getInstance().getRemainCount(roomId);
		if(holder > 0 && holder != userId && otherRemain > 0){
			error = 4;
			errDesc = "其他玩家正在玩，请稍后重试";
			PacketManager.send(userId, this);
			logger.info("User({}) get catcher failed because other({}) holding machine in room({}), anchorId({})", userId, holder, roomId, anchorId);
			return;
		}
		/*int cost = count * 100;
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
				RoomCardBill.AttrSOURCE_NAME.set("直播房间("+roomId+")内玩娃娃机"+count+"次"),
				RoomCardBill.AttrAMOUNT.set(-cost),
				RoomCardBill.AttrBEFORE_BAL.set(oldRoomCardCount),
				RoomCardBill.AttrAFTER_BAL.set(nowRoomCardCount),
				RoomCardBill.AttrCREATE_TIME.set(now));*/
		error = 0;
		CatcherModel.getInstance().addHolder(roomId, userId, count);
		PacketManager.send(userId, this);
		QingMengService.handle("start", anchorId, mac.MACHINE_MAC);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		wannaplay.request request = wannaplay.request.parseFrom(bytes);
		roomId = request.getRoomId();
		count = request.getCount();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		wannaplay.response.Builder builder = wannaplay.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setName(name);
		builder.setCount(otherRemain);
		
		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
