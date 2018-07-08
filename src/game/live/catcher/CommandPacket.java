package game.live.catcher;

import com.mozat.morange.dbcache.tables.AnchorMachine;

import game.live.model.LiveRoomService;
import game.live.qingmeng.QingMengService;
import game.packet.Packet;
import game.packet.PacketManager;
import protocols.header.packet.Builder;
import protocols.live.catcher.command;

public class CommandPacket extends Packet {
	//请求
	private int roomId;
	private String userCmd;
	
	//下发
	private int error;
	private int remainCount;
	
	@Override
	public void execPacket(){
		int anchorId = LiveRoomService.getRoomAnchor(roomId);
		if(anchorId == -1){
			error = 1;
			errDesc = "房间已停播";
			PacketManager.send(userId, this);
			logger.info("User({}) operate catcher failed because can't find room({})", userId, roomId);
			return;
		}
		AnchorMachine mac = AnchorMachine.getOneByCriteria(AnchorMachine.AttrANCHOR_ID.eq(anchorId),AnchorMachine.AttrIS_ONLINE.eq("1"));
		if(mac == null){
			error = 2;
			errDesc = "主播未链接娃娃机";
			PacketManager.send(userId, this);
			logger.info("User({}) operate catcher failed because no machine online in room({}), anchorId({})", userId, roomId, anchorId);
			return;
		}
		int holder = CatcherModel.getInstance().getHolder(roomId);
		if(holder > 0 && holder != userId){
			error = 3;
			errDesc = "其他玩家正在玩，请稍后重试";
			PacketManager.send(userId, this);
			logger.info("User({}) operate catcher failed because other({}) holding machine in room({}), anchorId({})", userId, holder, roomId, anchorId);
			return;
		}
		if("catch".equals(userCmd)){
			remainCount = CatcherModel.getInstance().decrease(roomId);
		}
		if(remainCount >= 0){
			error = 0;
			QingMengService.handle(userCmd, anchorId, mac.MACHINE_MAC);
		} else {
			error = 4;
			errDesc = "你的操作次数已用完，无法操作，请重新购买次数";
		}
		PacketManager.send(userId, this);
		if("catch".equals(userCmd)){
			QingMengService.handle("start", anchorId, mac.MACHINE_MAC);
		}
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		command.request request = command.request.parseFrom(bytes);
		roomId = request.getRoomId();
		userCmd = request.getCommand();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		command.response.Builder builder = command.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		
		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
