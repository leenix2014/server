package game.businesspackets;

import game.packet.Packet;
import netty.GameCommon.DismissRoomHandler;
import protocols.header.packet.Builder;

public class DismissRoomPacket extends Packet{
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
		DismissRoomHandler handler = new DismissRoomHandler(this.body, this.userId);
		handler.processHandle();
	}
	
}
