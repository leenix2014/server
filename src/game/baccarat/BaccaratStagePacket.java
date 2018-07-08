package game.baccarat;

import game.baccarat.model.BaccaratRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.baccarat.stage;

public class BaccaratStagePacket extends Packet{
	//请求
	
	//下发
	public String currStage;
	public int countDown;
	
	@Override
	public void execPacket(){
		BaccaratRoomService.tellStage(this);
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
