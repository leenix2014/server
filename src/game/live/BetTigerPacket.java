package game.live;

import game.live.model.LiveRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.live.bettiger;

public class BetTigerPacket extends Packet{
	//请求
	public int roomId;
	public int line;
	public int bet;
	
	//下发
	public int betRemain;
	
	@Override
	public void execPacket(){
		LiveRoomService.betTiger(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		bettiger.request request = bettiger.request.parseFrom(bytes);
		roomId = request.getRoomId();
		line = request.getLine();
		bet = request.getBet();
		if(bet <= 0){
			bet = 10;
		}
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		bettiger.response.Builder builder = bettiger.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setBetRemain(betRemain);
		
		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
