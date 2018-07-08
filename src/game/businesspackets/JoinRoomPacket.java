package game.businesspackets;

import game.packet.Packet;
import netty.GameCommon.JoinRoomHandler;
import protocols.header.packet.Builder;

public class JoinRoomPacket extends Packet{
	//请求
	private byte[] body;
	
	@Override
	public void readBody(byte[] bytes) throws Exception {
		this.body = bytes;
	}

	@Override
	public void writeBody(Builder pktBuilder) {
		
	}

	@Override
	public void execPacket() throws Exception {
		JoinRoomHandler handler = new JoinRoomHandler(this.body, this.userId);
		handler.processHandle();
	}
	
}
