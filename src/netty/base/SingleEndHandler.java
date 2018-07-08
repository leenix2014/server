package netty.base;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mozat.morange.dbcache.tables.CoinBill;

import game.packet.PacketManager;
import game.packet.PacketTypes;
import game.task.TaskService;
import netty.GameCommon.DismissRoomPollingHandler;
import netty.GameCommon.GameAllOverHandler;
import netty.GameCommon.GameStartHandler;
import netty.GameEvaluation.BullCardsConstantInterface;
import netty.GameModels.DismissMgr;
import netty.GameModels.GameBetMgr;
import netty.GameModels.GameGrabMgr;
import netty.GameModels.GameInningMgr;
import netty.GameModels.GameResultMgr;
import netty.GameModels.GameRoomMgr;
import netty.GameModels.PlayerCardsMgr;
import netty.GameModels.TimerTaskMgr;
import netty.GameModels.UserMgr;
import netty.ThreeCardGameProcess.ThreeCardDealingHander;
import netty.interf.SingleOverHandler;
import netty.util.MathUtil;
import protocols.config;
import protocols.header;
import protocols.bull.over;

public class SingleEndHandler implements SingleOverHandler {
	
	protected int roomId;
	
	private Set<Integer> kickOutUsers = new HashSet<>();
	
	public SingleEndHandler(int roomId){
		this.roomId = roomId;
	}
	
	@Override
	public int getRoomId() {
		return roomId;
	}
	
	protected void onCleanRecordInfo() {
		GameGrabMgr.getInstance().deleteRoomGrabs(this.roomId);
		GameBetMgr.getInstance().deleteRoomBets(this.roomId);
	}
	
	protected boolean isPlayerReachTop(){
		int pmscores = GameRoomMgr.getInstance().getRoomConfig(this.roomId).pmscores;
		if(pmscores <= 0){
			return false;
		}
		Set<Integer> userIds = GameRoomMgr.getInstance().getRoomUsers(this.roomId);
		int dealerId = GameGrabMgr.getInstance().getDealerId(roomId);
		for(Integer userId : userIds){
			//房主坐庄时，需忽略庄家
			if(GameRoomMgr.getInstance().getRoomConfig(roomId).dealer == config.bull.DEALER.OWNER && userId == dealerId){
				continue;
			}
			int total = GameResultMgr.getInstance().onGetTotalResult(this.roomId, userId);
			//只对输封顶
			if(total < 0 && MathUtil.abs(total) >= pmscores){
				return true;
			}
		}
		return false;
	}
	
	protected boolean isDealerReachTop(){
		int dmscores = GameRoomMgr.getInstance().getRoomConfig(this.roomId).dmscores;
		if(dmscores <= 0){
			return false;
		}
		int dealerId = GameGrabMgr.getInstance().getDealerId(roomId);
		int total = GameResultMgr.getInstance().onGetTotalResult(this.roomId, dealerId);
		//只对输封顶
		if(total < 0 && MathUtil.abs(total) >= dmscores){
			return true;
		}
		return false;
	}
	
	public void notifyOver(){
		int gameType = GameRoomMgr.getInstance().getRoomGameType(roomId);
		GameResultMgr.getInstance().compareResults(this.roomId);
		
		over.response.Builder respBuilder = over.response.newBuilder();

		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.SINGLE_GAMEOVER_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
    	
        respBuilder.setInning(GameInningMgr.getInstance().getCurrInning(this.roomId));	// 第几局

        int dealerSeat = GameGrabMgr.getInstance().getDealerSeat(this.roomId);
        respBuilder.setDealer(dealerSeat);	// 庄家座位号
        
        Map<Integer, List<Integer> > playerCards = PlayerCardsMgr.getInstance().getRoomHandCard(this.roomId);
		for (Map.Entry<Integer, List<Integer>> entry : playerCards.entrySet()) {
			int userId = entry.getKey();
			int seatId = GameRoomMgr.getInstance().getSeatId(roomId, userId);
			over.seat_t.Builder seatInfo = over.seat_t.newBuilder();
			seatInfo.setId(seatId);	// 座位ID
			List<Integer> cards = entry.getValue();
		    for (int i = 0; i < cards.size(); ++i) {
		    	seatInfo.addShow(cards.get(i));
			}
		    int score = GameResultMgr.getInstance().onGetSingleResult(this.roomId, userId);
		    //添加任务计数
		    if(score > 0 && !GameRoomMgr.getInstance().isDemoRoom(this.roomId)) {
		    	if(userId != -1) {
		    		TaskService.addTaskDoneCount(userId, gameType == BullCardsConstantInterface.ThreeCardGame?TaskService.TASK_TYPE_35CARD_WIN:TaskService.TASK_TYPE_BULL_WIN, 1);
		    	}
		    }
		    boolean coinroom = GameRoomMgr.getInstance().isCoinRoom(this.roomId);
		    int draw = Math.round(GameResultMgr.getInstance().getSingleDraw(this.roomId, userId));
		    if(coinroom && score != 0){
		    	int oldCoin = UserMgr.getInstance().getUserCoin(userId);
		    	int nowCoin = oldCoin + score - draw;
		    	if(nowCoin<=0){
		    		// 踢出玩家
		    		kickOutUsers.add(userId);
		    	} else {
		    		kickOutUsers.remove(userId);
		    	}
		    	UserMgr.getInstance().setUserCoin(userId, nowCoin);
		    	CoinBill.create(CoinBill.AttrUSER_ID.set(userId+""),
						CoinBill.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(userId)),
						CoinBill.AttrSOURCE.set("coinroom"),
						CoinBill.AttrSOURCE_ID.set(this.roomId+""),
						CoinBill.AttrSOURCE_NAME.set("金币场私人房("+this.roomId+")"+(score>=0?"赢":"输")+"钱"),
						CoinBill.AttrAMOUNT.set(score - draw),
						CoinBill.AttrBEFORE_BAL.set(oldCoin),
						CoinBill.AttrAFTER_BAL.set(nowCoin),
						CoinBill.AttrCREATE_TIME.set(new Date()));
		    }
		    seatInfo.setRank(PlayerCardsMgr.getInstance().getCardType(this.roomId, userId));	// 牌型
		    seatInfo.setScores(coinroom?score-draw:score);	// 单局分数
		    seatInfo.setDraw(draw);
        	seatInfo.setTotal(coinroom?UserMgr.getInstance().getUserCoin(userId):GameResultMgr.getInstance().onGetTotalResult(this.roomId, userId));	// 总分
        	seatInfo.setMultiple(GameBetMgr.getInstance().getPlayerBet(this.roomId, userId)); 	// 倍数

        	respBuilder.addSeats(seatInfo);
		}
        
    	head.setBody(respBuilder.buildPartial().toByteString());
		
    	byte[] msgContent = head.buildPartial().toByteArray();
    	
    	Set<Integer> uidSet = GameRoomMgr.getInstance().getRoomUsers(this.roomId);
		for (Integer userId : uidSet) {
			PacketManager.send(userId, msgContent);
		}
		
		// 延时一定是时间再调用onGameOverNextHandle
		TimerTaskMgr.getInstance().laterHandleGameOver(this);
	}
	
	@Override
	public void onGameOverNextHandle() {
		int gameType = GameRoomMgr.getInstance().getRoomGameType(roomId);
		int status = PlayerCardsMgr.getInstance().getPhaseStatus(this.roomId);
		int reason = -1;
		if (GameInningMgr.getInstance().isReachRoomFixedInning(this.roomId)) {
			reason = 0;// 已达到房间总局数
		} else if(isPlayerReachTop()){
			reason = 2;// 玩家封顶
		} else if(isDealerReachTop()){
			reason = 3;// 庄家封顶
		} else if(GameRoomMgr.getInstance().isAllOffline(this.roomId)){
			reason = 4;// 所有人离线
		}
		if(reason >= 0 && (BullCardsConstantInterface.BullGame == gameType || 
				(BullCardsConstantInterface.ThreeCardGame == gameType && status == DealingHander.PHASE2))){
			new GameAllOverHandler(this.roomId, reason).processHandle();
			return;
		}
		if(BullCardsConstantInterface.ThreeCardGame == gameType){
			PlayerCardsMgr.getInstance().clearCardType(this.roomId);
			if (status == DealingHander.PHASE1) {
				new ThreeCardDealingHander(this.roomId).twoPhaseDealing(DealingHander.PHASE2);;
				return;
			} 
		}
		if (DismissMgr.getInstance().needDismiss(this.roomId)) {
			// 当局游戏结束，发起解散房间投票
			new DismissRoomPollingHandler(this.roomId).startDismissVote();
			return;
		}
		if(BullCardsConstantInterface.BullGame == gameType || 
				(BullCardsConstantInterface.ThreeCardGame == gameType && status == DealingHander.PHASE2)){
			onCleanRecordInfo();
			for(Integer userId: kickOutUsers){
				GameRoomMgr.getInstance().quitRoom(roomId, userId);
			}
			if(GameRoomMgr.getInstance().getRoomUserCount(roomId) < 2){
				new GameAllOverHandler(this.roomId, 5).processHandle();//玩家人数少于2
				return;
			}
		}
		// 开始下一局游戏
		new GameStartHandler(this.roomId).processHandle();
	}
}
