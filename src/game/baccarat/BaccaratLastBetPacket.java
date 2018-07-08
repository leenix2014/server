package game.baccarat;

import java.util.Map;

import game.baccarat.model.BaccaratRoomModel;
import game.baccarat.model.BaccaratRoomService;
import game.packet.Packet;
import protocols.header.packet.Builder;
import protocols.baccarat.lastbet;
import protocols.baccarat.lastbet.bet_reward;

public class BaccaratLastBetPacket extends Packet{
	//请求
	public String roomId;//如果允许同时加入多个轮盘，则有用。保留
	
	//下发
	public BaccaratRoomModel model;
	
	@Override
	public void execPacket(){
		BaccaratRoomService.lastbet(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		lastbet.request req = lastbet.request.parseFrom(bytes);
		roomId = req.getRoomId();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		lastbet.response.Builder builder = lastbet.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		if(model != null){
			Map<String, Integer> lastBet = model.getLastBet(userId);
			Map<String, Integer> lastReward = model.getLastReward(userId);
			for(Map.Entry<String, Integer> entry : lastBet.entrySet()){
				String target = entry.getKey();
				bet_reward.Builder bld = bet_reward.newBuilder();
				bld.setTarget(target);
				bld.setCoins(entry.getValue());
				Integer reward = lastReward.get(target);
				if(reward == null){
					reward = 0;
				}
				bld.setReward(reward);
				builder.addBets(bld);
			}
		}

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
