package game.user.packet;

import java.util.Map;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mozat.morange.dbcache.tables.TUsers;

import game.packet.PacketManager;
import game.packet.PacketTypes;
import game.user.Users;
import netty.GameModels.GameInningMgr;
import netty.GameModels.GameRoomMgr;
import netty.GameModels.GameRoomMgr.RoomConfig;
import netty.GameModels.UserMgr;
import netty.util.MathUtil;
import protocols.config;
import protocols.header;
import protocols.header.packet.Builder;
import protocols.bull.reconnect;

public class ReconnectPacket extends LoginBasePacket{
	//请求
	reconnect.reconRequest request;
	
	//下发
	
	@Override
	public void execPacket() {
		this.sid = request.getSid();
		this.uuid = request.getUuid();
		this.token = request.getToken();
		this.extra = request.getExtra();
		
		logger.info("User reconnect, sid("+sid+"),param("+uuid+"),token("+token+"),extra("+extra+")");
		if(sid <= 0 || sid >= 4){
			logger.info("User reconnect failed because sid not correct, sid("+sid+"),param("+uuid+"),token("+token+"),extra("+extra+")");
			reconnectFailed();
			return;
		}
		TUsers users = getLoginUser();
		if (users == null) {
			logger.info("User reconnect failed because can't find userId, sid("+sid+"),param("+uuid+"),token("+token+"),extra("+extra+")");
			reconnectFailed();
			return;
		}
		int userUID = MathUtil.parseInt(users.LOGIN_ID);
		int roomID = GameRoomMgr.getInstance().getRoomId(userUID);
    	reconnect.reconResponse.Builder respBuilder = reconnect.reconResponse.newBuilder();
    	
    	header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.RECONNECT_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	
    	respBuilder.setError(0);	// 错误代码，0=成功
    	respBuilder.setErrDesc("");
    	respBuilder.setInst(roomID==-1?0:roomID);	// 房间ID
    	
    	if(roomID != -1){
	    	respBuilder.setOwner(GameRoomMgr.getInstance().getOwnerSeat(roomID));	// 房主 seatID
	    	respBuilder.setPlaying(GameInningMgr.getInstance().isGameStarted(roomID));	// 牌局是否在进行
	    	respBuilder.setInning(GameInningMgr.getInstance().getCurrInning(roomID));	// 局数，每局开始后+1
	
	    	RoomConfig gameConfig = GameRoomMgr.getInstance().getRoomConfig(roomID);
	    	config.bull.Builder bullConfigBuilder = config.bull.newBuilder();
			bullConfigBuilder.setDealer(gameConfig.dealer);
			bullConfigBuilder.setBscores(gameConfig.bscores);
			bullConfigBuilder.setSeats(gameConfig.seats);
			bullConfigBuilder.setInnings(gameConfig.innings);
			bullConfigBuilder.setPmscores(gameConfig.pmscores);
			bullConfigBuilder.setDmscores(gameConfig.dmscores);
			bullConfigBuilder.setBlind(gameConfig.blind);
			respBuilder.setConfig(bullConfigBuilder);	// 游戏配置
			
			Map<Integer, Integer> userSeat = GameRoomMgr.getInstance().getSeatMap(roomID);
			for(Map.Entry<Integer, Integer> entry : userSeat.entrySet()){
				int userId = entry.getKey();
				int seatId = entry.getValue();
				String name = "";
				int avatarId = 1;
				TUsers tempUsers = Users.load(userId);
				if(tempUsers != null){
					name = tempUsers.NAME;
					avatarId = tempUsers.AVATAR_ID;
				}
				
				int roomcard = UserMgr.getInstance().getCuber(userId);
				
				config.seat_t.Builder seat = config.seat_t.newBuilder();
				seat.setId(seatId);
				seat.setUid(userId);
				seat.setName(name);
				seat.setAvatar(avatarId);
				seat.setRoomcard(roomcard);
				
				respBuilder.addSeats(seat);	// 房间内所有玩家座位信息
			}
    	}
    	logger.info("User("+users.LOGIN_ID+") reconnect success, gaming room("+roomID+"), sid("+sid+"),param("+uuid+"),token("+token+"),extra("+extra+")");
    	head.setBody(respBuilder.buildPartial().toByteString());
		
    	byte[] msgContent = head.buildPartial().toByteArray();
    	PacketManager.send(userUID, msgContent);
	}
	
	private void reconnectFailed(){
		reconnect.reconResponse.Builder builder = reconnect.reconResponse.newBuilder();
    	
    	header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.RECONNECT_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	
    	builder.setError(1);
    	builder.setErrDesc("重连失败");
    	
    	head.setBody(builder.buildPartial().toByteString());
		
    	byte[] msgContent = head.buildPartial().toByteArray();
    	PacketManager.send(session, msgContent);
	}

	@Override
	public void readBody(byte[] bytes) throws InvalidProtocolBufferException {
		request = reconnect.reconRequest.parseFrom(bytes);	
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		
	}
}
