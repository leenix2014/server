package netty.GameCommon;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.packet.PacketManager;
import game.packet.PacketTypes;
import netty.BullGameProcess.BullDealingHander;
import netty.GameEvaluation.BullCardsConstantInterface;
import netty.GameModels.GameGrabMgr;
import netty.GameModels.GameInningMgr;
import netty.GameModels.GameRoomMgr;
import netty.GameModels.TimerTaskMgr;
import netty.ThreeCardGameProcess.ThreeCardDealingHander;
import netty.base.DealingHander;
import protocols.config;
import protocols.header;
import protocols.bull.start;

public class GameStartHandler {
	
	private static Logger logger = LoggerFactory.getLogger(GameStartHandler.class);
	
	private int roomId;
	
	public GameStartHandler(int roomID) {
		this.roomId = roomID;
	}
	
	public void processHandle() {
		onResponseDataHandle();
	}
	
	// 游戏开始
	private void onResponseDataHandle(){
		GameInningMgr.getInstance().inningIncrement(roomId);
		logger.info("Starting game in room("+roomId+"), round:"+GameInningMgr.getInstance().getCurrInning(roomId));
		
		//一局扣一张房卡
		GameRoomMgr.getInstance().consumeRoomCard(roomId);
		
		start.response.Builder respBuilder = start.response.newBuilder();
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.START_GAME_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	
    	respBuilder.setError(0);
    	respBuilder.setErrDesc("");
    	respBuilder.setInning(GameInningMgr.getInstance().getCurrInning(roomId));
    	if (GameRoomMgr.getInstance().getRoomConfig(roomId).dealer == config.bull.DEALER.OWNER ||
    			GameRoomMgr.getInstance().getRoomConfig(roomId).dealer == config.bull.DEALER.WINNER) {
    		respBuilder.setDealer(GameGrabMgr.getInstance().getDealerSeat(roomId));
    	} else {
    		//抢庄和通比模式下，开始游戏时没有庄家。
    		respBuilder.setDealer(0);
    	}
    	head.setBody(respBuilder.buildPartial().toByteString());
		
    	byte[] msgContent = head.buildPartial().toByteArray();
    	Set<Integer> uidSet = GameRoomMgr.getInstance().getRoomUsers(roomId);
		for (Integer userId : uidSet) {
			PacketManager.send(userId, msgContent);
		}
		
		// 设置定时器
		TimerTaskMgr.getInstance().genTimer(roomId);
		
		if (GameRoomMgr.getInstance().isWatchBet(this.roomId)){
			int gameType = GameRoomMgr.getInstance().getRoomGameType(roomId);
			if (gameType == BullCardsConstantInterface.ThreeCardGame) {
				new ThreeCardDealingHander(roomId).twoPhaseDealing(DealingHander.PHASE1);
			} 
			else if (gameType == BullCardsConstantInterface.BullGame) {
				new BullDealingHander(roomId).twoPhaseDealing(DealingHander.PHASE1);
			}
			return;
		}
		if (GameRoomMgr.getInstance().getRoomConfig(roomId).dealer == config.bull.DEALER.GRAB) {
			TimerTaskMgr.getInstance().laterGrabTimeout(roomId);
		}
		else {
			TimerTaskMgr.getInstance().laterBetTimeout(roomId);
		}
	}
}
