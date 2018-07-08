package game.baccarat;


import game.baccarat.model.BaccaratRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.baccarat.leave;

public class BaccaratLeavePacket extends Packet{
	//请求
	public String roomId;//Reserved
	public boolean forced;
	
	//下发
	
	@Override
	public void execPacket(){
		BaccaratRoomService.leave(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		leave.request req = leave.request.parseFrom(bytes);
		roomId = req.getRoomId();
		forced = req.getForced();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		leave.response.Builder builder = leave.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
