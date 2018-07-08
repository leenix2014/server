package game.roulette;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.packet.Packet;
import game.roulette.model.RouletteRoomModel;
import game.roulette.model.RouletteRoomService;
import game.roulette.util.ConvertUtil;
import netty.GameModels.UserMgr;
import protocols.header.packet.Builder;
import protocols.roulette.common.bet;
import protocols.roulette.confirmbet;

public class ConfirmBetPacket extends Packet{
	//请求
	public String roomId;//如果允许同时加入多个轮盘，则有用。保留
	public Map<String, Integer> mybet;//本次下注汇总
	
	//下发
	public RouletteRoomModel model;
	public List<bet> sumBet;//本次下注汇总
	
	@Override
	public void execPacket(){
		RouletteRoomService.bet(this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		confirmbet.request request = confirmbet.request.parseFrom(bytes);
		List<bet> bets = request.getBetsList();
		mybet = new HashMap<String, Integer>();
    	for(bet inbet : bets){
    		String target = inbet.getTarget();
    		int coins = inbet.getCoins();
    		if(coins <= 0){
    			coins = 1;
    		}
    		Integer lastbet = mybet.get(target);
    		if(lastbet == null){
    			mybet.put(target, coins);
    		} else {
    			mybet.put(target, lastbet + coins);
    		}
     	}
    	sumBet = new ArrayList<>();
    	for(Map.Entry<String, Integer> entry : mybet.entrySet()){
    		sumBet.add(ConvertUtil.toBet(entry.getKey(), entry.getValue()));
    	}
		logger.info("User({}) bet {} in roulette", userId, bets.toString());
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		confirmbet.response.Builder builder = confirmbet.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		if(model != null){
			builder.addAllBets(sumBet);
			
			Map<String, Integer> myBet = model.getUserBet(userId);
			for(Map.Entry<String, Integer> entry : myBet.entrySet()){
				bet.Builder b = bet.newBuilder();
				b.setTarget(entry.getKey());
				b.setCoins(entry.getValue());
				builder.addSelfBets(b);
			}
			builder.setUserCoin(UserMgr.getInstance().getUserCoin(userId));
		}

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
