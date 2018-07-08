package game.live;

import game.live.model.LiveRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.live.betfruit;
import protocols.live.betfruit.fruit_bet;

public class BetFruitPacket extends Packet{
	//请求
	public int roomId;
	public fruit_bet bet;
	
	//下发
	public int betRemain;
	
	@Override
	public void execPacket(){
		LiveRoomService.betFruit(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		betfruit.request req = betfruit.request.parseFrom(bytes);
		roomId = req.getRoomId();
		bet = req.getBet();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		betfruit.response.Builder builder = betfruit.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setBetRemain(betRemain);
		
		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
