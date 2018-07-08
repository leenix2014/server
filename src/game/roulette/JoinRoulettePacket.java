package game.roulette;


import java.util.Map;

import game.packet.Packet;
import game.roulette.model.RouletteRoomModel;
import game.roulette.model.RouletteRoomService;
import game.roulette.util.ConvertUtil;
import protocols.header.packet.Builder;
import protocols.roulette.joinroom;

public class JoinRoulettePacket extends Packet{
	//请求
	public String roomId;
	
	//下发
	public RouletteRoomModel model;
	
	@Override
	public void execPacket(){
		RouletteRoomService.join(this);
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
			builder.setConfig(ConvertUtil.toRoomInfo(model.getConfig()));
			builder.setSelf(ConvertUtil.toUserInfo(userId, model));
			
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
			builder.setResult(model.getResult());
			Map<String, Integer> myReward = model.getUserReward(userId);
			for(Map.Entry<String, Integer> entry : myReward.entrySet()){
				builder.addSelfRewards(ConvertUtil.toBet(entry.getKey(), entry.getValue()));
			}
			
			builder.setHistoryBet(model.getHistoryBet(userId));
			builder.setHistoryReward(model.getHistoryReward(userId));
			model.getRoomUser();
			
			builder.setIsAdmin(model.isAdmin(userId));
		}

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
