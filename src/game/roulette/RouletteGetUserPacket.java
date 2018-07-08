package game.roulette;


import game.packet.Packet;
import game.roulette.model.RouletteRoomModel;
import game.roulette.model.RouletteRoomService;
import game.roulette.util.ConvertUtil;
import protocols.header.packet.Builder;
import protocols.roulette.getuser;

public class RouletteGetUserPacket extends Packet{
	//请求
	
	//下发
	public RouletteRoomModel model;
	
	@Override
	public void execPacket(){
		RouletteRoomService.getUser(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		//getuser.request request = getuser.request.parseFrom(bytes);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		getuser.response.Builder builder = getuser.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		if(model != null){
			for(Integer user : model.getRoomUser()){
				builder.addPlayers(ConvertUtil.toUserInfo(user, model));
			}
		}

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
