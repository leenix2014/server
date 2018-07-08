package game.coinroom;

import game.coinroom.model.CoinRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.coinroom.start;

public class CoinRoomStartPacket extends Packet{
	//请求
	
	//下发
	public int error;
	
	@Override
	public void execPacket(){
		CoinRoomService.start(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		start.response.Builder builder = start.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
