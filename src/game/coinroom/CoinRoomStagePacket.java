package game.coinroom;

import game.coinroom.model.CoinRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.coinroom.common.room_stage;
import protocols.coinroom.stage;

public class CoinRoomStagePacket extends Packet{
	//请求
	
	//下发
	public int error;
	public room_stage curr;
	public int countDown;
	
	@Override
	public void execPacket(){
		CoinRoomService.stage(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		stage.response.Builder builder = stage.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setCurr(curr);
		builder.setCountDown(countDown);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
