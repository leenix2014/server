package game.coinroom;

import game.coinroom.model.CoinRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.coinroom.bet;

public class CoinRoomBetPacket extends Packet{
	//请求
	public int multiple;
	
	//下发
	public int error;
	public String errDesc = "";
	public int myBet;
	
	@Override
	public void execPacket(){
		CoinRoomService.bet(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		bet.request req = bet.request.parseFrom(bytes);
		multiple = req.getBet();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		bet.response.Builder builder = bet.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setBet(myBet);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
