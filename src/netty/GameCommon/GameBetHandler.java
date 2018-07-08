package netty.GameCommon;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;

import game.packet.PacketManager;
import game.packet.PacketTypes;
import netty.BullGameProcess.BullDealingHander;
import netty.GameEvaluation.BullCardsConstantInterface;
import netty.GameModels.GameBetMgr;
import netty.GameModels.GameRoomMgr;
import netty.GameModels.GameRoomMgr.RoomConfig;
import netty.GameModels.TimerTaskMgr;
import netty.ThreeCardGameProcess.ThreeCardDealingHander;
import netty.base.DealingHander;
import protocols.config;
import protocols.header;
import protocols.bull.betting;

public class GameBetHandler {
	
	private static Logger logger = LoggerFactory.getLogger(GameBetHandler.class);
	
	private byte[] body;
	
	private int userId;
	
	private int roomID;
	
	private int bets;
	
	public GameBetHandler(int roomID) {
		this.roomID = roomID;
	}
	
	public GameBetHandler(int roomID, int userId, int bet) {
		this.roomID = roomID;
		this.userId = userId;
		this.bets = bet;
	}
	
	public GameBetHandler(byte[] body, int userId) {
		this.body = body;
		this.userId = userId;
		this.roomID = GameRoomMgr.getInstance().getRoomId(this.userId);
	}
	
	public void processHandle() {
		
		onRequestDataHandle();
		
		onResponseDataHandle();
	}
	
	private betting.request onDecodecMsg() throws InvalidProtocolBufferException{
		betting.request requestMsg = betting.request.parseFrom(this.body);
		return requestMsg;
	}
	
	private void onRequestDataHandle(){
		try {
			betting.request requestMsg = onDecodecMsg();
			this.bets = requestMsg.getBets();
			logger.info("User("+this.userId+") bet "+this.bets+" in room("+this.roomID+")");
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}
	
	public void onResponseDataHandle(){
		
		GameBetMgr.getInstance().setPlayerBet(roomID, this.userId, this.bets);
		
		boolean isAllPlayerBet = GameBetMgr.getInstance().isAllBet(this.roomID);

		broadcastBetInfo(this.userId, this.bets, isAllPlayerBet);
		
		if (isAllPlayerBet) {
			afterAllBet();
		}
	}
	
	private void broadcastBetInfo(int userId, int bet, boolean isAllPlayerBet) {
		betting.response.Builder respBuilder = betting.response.newBuilder();
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.GAME_BET_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	RoomConfig roomConfig = GameRoomMgr.getInstance().getRoomConfig(roomID);
    	if((roomConfig.dealer == config.bull.DEALER.OWNER || roomConfig.dealer == config.bull.DEALER.WINNER)
    		&& bet > roomConfig.maxBet){
    		respBuilder.setError(1);// 1=下注倍数超过房间最大倍数
    		respBuilder.setErrDesc("下注倍数超过房间最大倍数");
        	respBuilder.setBets(GameBetMgr.getInstance().defaultBet());
    	} else {
	    	respBuilder.setError(0);
	    	respBuilder.setErrDesc("");
	    	respBuilder.setBets(bet);
    	}
    	respBuilder.setSeat(GameRoomMgr.getInstance().getSeatId(roomID, userId));
    	respBuilder.setIsAllPlayerBet(isAllPlayerBet?1:0);
    	
    	head.setBody(respBuilder.buildPartial().toByteString());
		
    	byte[] msgContent = head.buildPartial().toByteArray();
    	
    	Set<Integer> uidSet = GameRoomMgr.getInstance().getRoomUsers(this.roomID);
		for (Integer uid : uidSet) {
			PacketManager.send(uid, msgContent);
		}
	}
	
	// 开始发牌
	public void afterAllBet() {
		if(GameBetMgr.getInstance().isAllBetHandled(roomID)){
			return;//防止重复执行
		}
		GameBetMgr.getInstance().setAllBetHandled(roomID);
		int gameType = GameRoomMgr.getInstance().getRoomGameType(this.roomID);
		if(gameType == BullCardsConstantInterface.ThreeCardGame) {
			if (GameRoomMgr.getInstance().isWatchBet(roomID)) {
				broadcastBetInfo(this.userId, this.bets, true);//看牌下注第一轮在GameStart发牌
			} else {
				//不看牌下注第一轮在下完注后发牌
				new ThreeCardDealingHander(this.roomID).twoPhaseDealing(DealingHander.PHASE1);
			}
			//三五张第二轮在游戏结算第一轮后发牌
		} 
		
		if (gameType == BullCardsConstantInterface.BullGame) {
			if (GameRoomMgr.getInstance().isWatchBet(roomID)) {
				//看牌下注第一轮在GameStart发牌，第二轮在下完注后发牌
				new BullDealingHander(this.roomID).twoPhaseDealing(DealingHander.PHASE2);
			} else {
				//不看牌下注在下完注后一次性发牌
				new BullDealingHander(this.roomID).oneTimeDealing();
			}
		}
		
		//TimerTaskMgr.getInstance().cancelTimerTask(roomID);//取消bet超时任务
		// 创建摊牌超时任务
		TimerTaskMgr.getInstance().laterAutoShowHand(this.roomID);
	}
}
