package game.baccarat;


import game.baccarat.model.BaccaratRoomModel;
import game.baccarat.model.BaccaratRoomService;
import game.baccarat.util.ConvertUtil;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.baccarat.getuser;

public class BaccaratGetUserPacket extends Packet{
	//请求
	
	//下发
	public BaccaratRoomModel model;
	
	@Override
	public void execPacket(){
		BaccaratRoomService.getUser(this);
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
			for(Integer user : model.getNonSeatedUser()){
				builder.addPlayers(ConvertUtil.toUserInfo(user, model));
			}
		}

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
