package game.baccarat;


import game.baccarat.model.BaccaratRoomModel;
import game.baccarat.model.BaccaratRoomService;
import game.baccarat.util.ConvertUtil;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.baccarat.getroom;

public class BaccaratGetRoomPacket extends Packet{
	//请求
	public String direction;
	
	//下发
	public BaccaratRoomModel model;
	
	@Override
	public void execPacket(){
		BaccaratRoomService.getRoom(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		getroom.request request = getroom.request.parseFrom(bytes);
		direction = request.getDirection();
		logger.info("User({}) get baccarat room, direction({})", userId, direction);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		getroom.response.Builder builder = getroom.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		if(model != null){
			builder.setConfig(ConvertUtil.toRoomInfo(model));
			builder.setWaybill(ConvertUtil.toWaybill(model.getWaybill()));
		}

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
