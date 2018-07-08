package game.roulette;

import java.util.Map;

import game.packet.Packet;
import game.roulette.model.RouletteRoomModel;
import game.roulette.model.RouletteRoomService;
import netty.GameModels.UserMgr;
import protocols.header.packet.Builder;
import protocols.roulette.common.bet;
import protocols.roulette.result;

public class ResultPacket extends Packet{
	//请求
	
	//下发
	public RouletteRoomModel model;
	
	@Override
	public void execPacket(){
		RouletteRoomService.tellResult(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		result.response.Builder builder = result.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		if(model != null){
			builder.setGameType(model.getConfig().GAME_TYPE);
			builder.setResult(model.getResult());
			builder.addAllWinTargets(model.getWinTargets());
			for(Map.Entry<String, Integer> entry : model.getUserReward(userId).entrySet()){
				String target = entry.getKey();
				int coins = entry.getValue();
				bet.Builder total = bet.newBuilder();
				total.setTarget(target);
				total.setCoins(coins);
				builder.addSelfRewards(total);
			}
			builder.setUserCoin(UserMgr.getInstance().getUserCoin(userId));
		}

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
