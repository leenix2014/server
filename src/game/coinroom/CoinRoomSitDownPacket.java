package game.coinroom;

import game.coinroom.model.CoinRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.coinroom.sitdown;

// to be used
public class CoinRoomSitDownPacket extends Packet{
	//请求
	public int seatId;
	
	//下发
	public int error;
	
	@Override
	public void execPacket(){
		CoinRoomService.sitdown(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		sitdown.request req = sitdown.request.parseFrom(bytes);
		seatId = req.getSeatId();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		sitdown.response.Builder builder = sitdown.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
