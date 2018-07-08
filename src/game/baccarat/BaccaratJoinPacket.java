package game.baccarat;


import java.util.Map;

import game.baccarat.model.BaccaratRoomModel;
import game.baccarat.model.BaccaratRoomService;
import game.baccarat.util.ConvertUtil;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.baccarat.joinroom;

public class BaccaratJoinPacket extends Packet{
	//请求
	public String roomId;
	
	//下发
	public BaccaratRoomModel model;
	
	@Override
	public void execPacket(){
		BaccaratRoomService.join(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		joinroom.request req = joinroom.request.parseFrom(bytes);
		roomId = req.getRoomId();
	}

	@Override
	public void writeBody(Builder pktBuilder) {
		joinroom.response.Builder builder = joinroom.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		
		if(model != null){
			builder.setConfig(ConvertUtil.toRoomInfo(model));
			
			builder.setSelf(ConvertUtil.toUserInfo(userId, model));
			
			for(Integer seatedUser : model.getSeatedUser()){
				builder.addPlayers(ConvertUtil.toUserInfo(seatedUser, model));
			}
			
			builder.setCurrStage(model.getStage());
			builder.setCountDown(model.getCountDown());
			
			Map<String, Integer> myBet = model.getUserBet(userId);
			for(Map.Entry<String, Integer> entry : myBet.entrySet()){
				builder.addSelfBets(ConvertUtil.toBet(entry.getKey(), entry.getValue()));
			}
			
			Map<String, Integer> totalBet = model.getTotalBet();
			for(Map.Entry<String, Integer> entry : totalBet.entrySet()){
				builder.addTotalBets(ConvertUtil.toBet(entry.getKey(), entry.getValue()));
			}
			Map<String, Integer> myReward = model.getUserReward(userId);
			for(Map.Entry<String, Integer> entry : myReward.entrySet()){
				builder.addSelfRewards(ConvertUtil.toBet(entry.getKey(), entry.getValue()));
			}
			builder.setIsAdmin(model.isAdmin(userId));
			builder.setWaybill(ConvertUtil.toWaybill(model.getWaybill()));
			builder.setHistoryBet(model.getHistoryBet(userId));
			builder.setHistoryReward(model.getHistoryReward(userId));
		}

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
