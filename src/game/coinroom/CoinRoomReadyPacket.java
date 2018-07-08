package game.coinroom;

import game.coinroom.model.CoinRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.coinroom.ready;

// to be used
public class CoinRoomReadyPacket extends Packet{
	//请求
	
	//下发
	
	@Override
	public void execPacket(){
		CoinRoomService.ready(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		
	}

	@Override
	public void writeBody(Builder pktBuilder) {
		ready.response.Builder builder = ready.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
