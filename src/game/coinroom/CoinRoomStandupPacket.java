package game.coinroom;

import game.coinroom.model.CoinRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.coinroom.standup;

// to be used
public class CoinRoomStandupPacket extends Packet{
	//请求
	
	//下发
	public int error;
	
	@Override
	public void execPacket(){
		CoinRoomService.standup(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		standup.response.Builder builder = standup.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
