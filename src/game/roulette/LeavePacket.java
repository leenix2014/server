package game.roulette;


import game.packet.Packet;
import game.roulette.model.RouletteRoomService;
import protocols.header.packet.Builder;
import protocols.roulette.leave;

public class LeavePacket extends Packet{
	//请求
	public String roomId;//Reserved
	public boolean forced;
	
	//下发
	
	@Override
	public void execPacket(){
		RouletteRoomService.leave(this);
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
