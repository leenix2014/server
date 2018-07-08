package game.roulette;

import game.packet.Packet;
import game.roulette.model.RouletteRoomService;
import protocols.header.packet.Builder;
import protocols.roulette.stage;

public class StagePacket extends Packet{
	//请求
	
	//下发
	public String currStage;
	public int countDown;
	
	@Override
	public void execPacket(){
		RouletteRoomService.tellStage(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		stage.response.Builder builder = stage.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setCurrStage(currStage);
		builder.setCountDown(countDown);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
