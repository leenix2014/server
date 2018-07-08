package game.user.packet;


import com.google.protobuf.InvalidProtocolBufferException;

import game.packet.Packet;
import game.packet.PacketManager;
import netty.GameModels.GameRoomMgr;
import protocols.header.packet.Builder;
import protocols.bull.gaming;

public class GamingPacket extends Packet{
	//请求
	gaming.request request;
	
	//下发
	public int error;
	public int inst;
	
	@Override
	public void execPacket() {
		int roomID = GameRoomMgr.getInstance().getRoomId(userId);
		this.error = 0;
		this.inst = roomID == -1? 0 : roomID;
		PacketManager.send(userId, this);
		logger.info("User({}) query gaming room({}) success!", userId, inst);
	}

	@Override
	public void readBody(byte[] bytes) throws InvalidProtocolBufferException {
		request = gaming.request.parseFrom(bytes);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		gaming.response.Builder builder = gaming.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc("");
		builder.setInst(inst);
		
		pktBuilder.setBody(builder.buildPartial().toByteString());	
	}
}
