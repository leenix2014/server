package game.businesspackets;

import game.packet.Packet;
import netty.GameCommon.GameShowHandHandler;
import protocols.header.packet.Builder;

public class ShowHandPacket extends Packet{
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
		GameShowHandHandler handler = new GameShowHandHandler(this.body, this.userId);
		handler.processHandle();
	}
	
}
