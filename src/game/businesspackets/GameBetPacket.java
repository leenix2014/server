package game.businesspackets;

import game.packet.Packet;
import netty.GameCommon.GameBetHandler;
import protocols.header.packet.Builder;

public class GameBetPacket extends Packet{
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
		GameBetHandler handler = new GameBetHandler(this.body, this.userId);
		handler.processHandle();
	}
	
}
