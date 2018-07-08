package game.businesspackets;

import game.packet.Packet;
import netty.GameCommon.GameStartHandler;
import netty.GameModels.GameRoomMgr;
import protocols.header.packet.Builder;

public class StartGamePacket extends Packet{
	//请求
	
	@Override
	public void readBody(byte[] bytes) throws Exception {
		
	}

	@Override
	public void writeBody(Builder pktBuilder) {
		
	}

	@Override
	public void execPacket() throws Exception {
		int roomId = GameRoomMgr.getInstance().getRoomId(this.userId);
		GameStartHandler handler = new GameStartHandler(roomId);
		handler.processHandle();
	}
	
}
