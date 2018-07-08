package game.live;

import game.live.model.LiveRoomModel;
import game.live.model.LiveRoomService;
import game.live.util.ConvertUtil;
import game.packet.Packet;
import netty.GameModels.UserMgr;
import protocols.header.packet.Builder;
import protocols.live.payroom;

public class PayLiveRoomPacket extends Packet{
	//请求
	public int roomId;
	public String pwd;
	
	//下发
	public LiveRoomModel model;
	
	@Override
	public void execPacket(){
		LiveRoomService.join(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		payroom.request request = payroom.request.parseFrom(bytes);
		roomId = request.getRoomId();
		pwd = request.getPwd();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		payroom.response.Builder builder = payroom.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setCube(UserMgr.getInstance().getUserCoin(userId));
		
		if(model != null){
			builder.setRoom(ConvertUtil.toRoomInfo(model, userId));
		}
		
		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
