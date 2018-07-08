package game.coinroom;

import java.util.List;

import game.coinroom.model.CoinRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.coinroom.showhand;

public class CoinRoomShowHandPacket extends Packet{
	//请求
	public List<Integer> handCards;
	
	//下发
	public int error;
	public int cardType;
	
	@Override
	public void execPacket(){
		CoinRoomService.showhand(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		showhand.request req = showhand.request.parseFrom(bytes);
		handCards = req.getHandList();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		showhand.response.Builder builder = showhand.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setCardType(cardType);
		builder.addAllHand(handCards);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
