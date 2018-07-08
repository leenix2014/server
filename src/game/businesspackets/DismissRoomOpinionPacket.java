package game.businesspackets;

import game.packet.Packet;
import netty.GameCommon.DismissRoomPollingHandler;
import protocols.header.packet.Builder;

public class DismissRoomOpinionPacket extends Packet{
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
		DismissRoomPollingHandler handler = new DismissRoomPollingHandler(this.body, this.userId);
		handler.processHandle();
	}
	
}
