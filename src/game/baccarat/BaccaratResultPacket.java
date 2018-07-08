package game.baccarat;

import java.util.Map;

import game.baccarat.model.BaccaratRoomModel;
import game.baccarat.model.BaccaratRoomService;
import game.baccarat.util.ConvertUtil;
import game.packet.Packet;
import netty.GameModels.UserMgr;
import protocols.header.packet.Builder;
import protocols.baccarat.common.baccarat_result;
import protocols.baccarat.result;
import protocols.baccarat.result.user_reward;


public class BaccaratResultPacket extends Packet{
	//请求
	
	//下发
	public BaccaratRoomModel model;
	
	@Override
	public void execPacket(){
		BaccaratRoomService.tellResult(this);
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
			baccarat_result.Builder result = baccarat_result.newBuilder();
			result.setBanker2Point(model.getBanker2Point());
			result.setBanker3Point(model.getBanker3Point());
			result.addAllBankerhands(model.getBankerHands());
			result.setPlayer2Point(model.getPlayer2Point());
			result.setPlayer3Point(model.getPlayer3Point());
			result.addAllPlayerHands(model.getPlayerHands());
			result.addAllWinTargets(model.getWinTargets());
			result.setRemains(model.getRemains());
			
			builder.setResult(result);
			
			for(Map.Entry<String, Integer> entry : model.getUserReward(userId).entrySet()){
				String target = entry.getKey();
				int coins = entry.getValue();
				builder.addSelfRewards(ConvertUtil.toBet(target, coins));
			}
			builder.setUserCoin(UserMgr.getInstance().getUserCoin(userId));
			
			for(Integer otherUser : model.getRoomUser()){
				user_reward.Builder ur = user_reward.newBuilder();
				ur.setPlayer(ConvertUtil.toUserInfo(otherUser, model));
				ur.setReward(model.getRewardTotal(otherUser));
				ur.setBigWinner(model.isBigWinner(userId));
				for(Map.Entry<String, Integer> detail : model.getUserReward(otherUser).entrySet()){
					ur.addDetail(ConvertUtil.toBet(detail.getKey(), detail.getValue()));
				}
				builder.addOtherRewards(ur);
			}
			builder.setRemains(model.getRemains());
			
			builder.setWaybill(ConvertUtil.toWaybill(model.getWaybill()));
			builder.setShuffle(model.isNeedShuffle());
		}

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
