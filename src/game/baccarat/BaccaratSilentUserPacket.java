package game.baccarat;


import game.baccarat.model.BaccaratRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.baccarat.silentuser;

public class BaccaratSilentUserPacket extends Packet{
	//请求
	public String roomId;
	public int silentUser;
	public boolean silent;
	
	//下发
	
	@Override
	public void execPacket(){
		BaccaratRoomService.silentUser(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		silentuser.request req = silentuser.request.parseFrom(bytes);
		roomId = req.getRoomId();
		silentUser = req.getUserId();
		silent = req.getSilent();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		silentuser.response.Builder builder = silentuser.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
