package netty.GameModels;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.mozat.morange.dbcache.tables.TRoomInnings;

import game.coinroom.model.CompareLogic;
import netty.GameEvaluation.BullCardsLogic;
import netty.base.DealingHander;
import protocols.config;

public class GameResultMgr {

	volatile private static GameResultMgr instance = null;
    
    private GameResultMgr(){
    }

    public static GameResultMgr getInstance() {

        if(instance == null){
            synchronized (GameResultMgr.class) {
                if(instance == null){
                    instance = new GameResultMgr();
                }
            }
        }

        return instance;
    }
    
    // 单局输赢分
    // Map<roomid, Map<userId, score>>
    private Map<Integer, Map<Integer, Integer>> singleMap = new HashMap<Integer, Map<Integer, Integer>>();
    // Map<roomid, Map<userId, draw>>
    private Map<Integer, Map<Integer, Float>> drawMap = new HashMap<Integer, Map<Integer, Float>>();
    // 总分
    // Map<roomid, Map<userId, totalScore>>
    private Map<Integer, Map<Integer, Integer>> totalMap = new HashMap<Integer, Map<Integer, Integer>>();
    // Map<roomid, Map<userId, totalDraw>>
    private Map<Integer, Map<Integer, Float>> totalDrawMap = new HashMap<Integer, Map<Integer, Float>>();
    
    // Map<roomId, Map<inning, phase>>
    private Map<Integer, Map<Integer, Integer>> allShowHandRoom = new HashMap<Integer, Map<Integer, Integer>>();
    
    public void setAllShowHand(int roomId){
    	synchronized (GameResultMgr.class) {
	    	int inning = GameInningMgr.getInstance().getCurrInning(roomId);
	    	int phase = PlayerCardsMgr.getInstance().getPhaseStatus(roomId);
	    	Map<Integer, Integer> innings = allShowHandRoom.get(roomId);
	    	if(innings == null){
	    		innings = new HashMap<Integer, Integer>();
	    	}
	    	innings.put(inning, phase);
	    	allShowHandRoom.put(roomId, innings);
    	}
    }
    
    public boolean isAllShowHand(int roomId){
    	synchronized (GameResultMgr.class) {
	    	int inning = GameInningMgr.getInstance().getCurrInning(roomId);
	    	int phase = PlayerCardsMgr.getInstance().getPhaseStatus(roomId);
	    	Map<Integer, Integer> innings = allShowHandRoom.get(roomId);
	    	if(innings == null){
	    		return false;
	    	}
	    	Integer allShowHandPhase = innings.get(inning);
	    	if(allShowHandPhase == null){
	    		return false;
	    	}
	    	return phase == allShowHandPhase;
    	}
    }
    
    public void clearResult(int roomId){
    	synchronized (GameResultMgr.class) {
    		singleMap.remove(roomId);
    		drawMap.remove(roomId);
    		totalMap.remove(roomId);
    		totalDrawMap.remove(roomId);
    		allShowHandRoom.remove(roomId);
    	}
    }
    
    // 房间内玩家比对牌型，计算结果分
    public void compareResults(int roomId) {
    	PlayerCardsMgr.getInstance().calBestCardType(roomId);
    	
    	Map<Integer, Integer> resultMap = null;
    	
    	if(GameRoomMgr.getInstance().isCoinRoom(roomId)){
    		int baseScore = GameRoomMgr.getInstance().getRoomBase(roomId);
    		int banker = GameGrabMgr.getInstance().getDealerId(roomId);
    		Map<Integer, Integer> playerBet = GameBetMgr.getInstance().getBetMap(roomId);
    		Map<Integer, Integer> playerCardType = PlayerCardsMgr.getInstance().getCardTypeMap(roomId);
    		CompareLogic comparer = new CompareLogic(baseScore, banker, playerBet, playerCardType);
    		if (GameRoomMgr.getInstance().getRoomConfig(roomId).dealer == config.bull.DEALER.NONE) {
    			comparer.compareToEachOther();
    		} else {
    			comparer.compareToBanker();
    		}
    		resultMap = comparer.getPlayerScore();
    	} else {
	    	if (GameRoomMgr.getInstance().getRoomConfig(roomId).dealer == config.bull.DEALER.NONE) {
	    		resultMap = compareToEachOther(roomId);
			} 
	    	else {
	    		resultMap = compareToDealer(roomId);
			}
    	}
    	
    	onUpdateGameResult(roomId, resultMap);
    }
    
    // 非通比模式只需和庄家比较
    // Map<userId, score>
    public Map<Integer, Integer> compareToDealer(int roomID) {
    	int roomBase = GameRoomMgr.getInstance().getRoomBase(roomID);
    	GameBetMgr betMgr = GameBetMgr.getInstance();
    	Map<Integer, Integer> cardTypes = PlayerCardsMgr.getInstance().getRoomCardTypes(roomID);
    	Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
    	int dealerScore = 0;
    	int dealerId = GameGrabMgr.getInstance().getDealerId(roomID);
		for(Map.Entry<Integer, Integer> entry : cardTypes.entrySet()) {
			int userId = entry.getKey();
			if (userId == dealerId) {
				continue;
			}
			int playerCardType = entry.getValue();
			int dealerCardType = cardTypes.get(dealerId);
			if (playerCardType > dealerCardType) {
				int score = roomBase * betMgr.getPlayerBet(roomID, userId) * BullCardsLogic.getCardTypeMul(playerCardType);
				resultMap.put(userId, score);
				dealerScore -= score;
			} else if (playerCardType == dealerCardType){
				resultMap.put(userId, 0);
			} else {
				int score = roomBase * betMgr.getPlayerBet(roomID, userId) * BullCardsLogic.getCardTypeMul(dealerCardType);
				resultMap.put(userId, -score);
				dealerScore += score;
			}
		}
		resultMap.put(dealerId, dealerScore);
		return resultMap;
	}
	
    // 通比模式算分
	public Map<Integer, Integer> compareToEachOther(int roomID) {
    	int roomBase = GameRoomMgr.getInstance().getRoomBase(roomID);
    	Map<Integer, Integer> cardTypes = PlayerCardsMgr.getInstance().getRoomCardTypes(roomID);
    	GameBetMgr betMgr = GameBetMgr.getInstance();
		Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
		
		for (Entry<Integer, Integer> entry: cardTypes.entrySet()) {
			int myId = entry.getKey();
			int myCardType = cardTypes.get(myId);
			for (Entry<Integer, Integer> otherEntry: cardTypes.entrySet()) {
				int otherId = otherEntry.getKey();
				int otherCardType = cardTypes.get(otherId);
				if (myId == otherId) {
					continue;
				}
				if (myCardType < otherCardType) {
					int sum = roomBase * betMgr.getPlayerBet(roomID, otherId) * BullCardsLogic.getCardTypeMul(otherCardType);
					if (resultMap.containsKey(myId)) {
						resultMap.put(myId, resultMap.get(myId) - sum);
					} 
					else {
						resultMap.put(myId, -sum);
					}
				}
				else if (myCardType > otherCardType) {
					int sum = roomBase * betMgr.getPlayerBet(roomID, myId) * BullCardsLogic.getCardTypeMul(myCardType);
					if (resultMap.containsKey(myId)) {
						resultMap.put(myId, resultMap.get(myId) + sum);
					} 
					else {
						resultMap.put(myId, sum);
					}
				}
			}
		}
		return resultMap;
	}
	
	// 更新单局分
	public void onUpdateGameResult(int roomId, Map<Integer, Integer> currResult) {
		synchronized (GameResultMgr.class) {
			// 单局分
			singleMap.put(roomId, currResult);
			Map<Integer, Float> sigleDrawMap = new HashMap<Integer, Float>();
			drawMap.put(roomId, sigleDrawMap);
			
			// 总分
			if (!totalMap.containsKey(roomId)) {
				totalMap.put(roomId, new HashMap<Integer, Integer>());
				totalDrawMap.put(roomId, new HashMap<Integer, Float>());
			}
			int inning = GameInningMgr.getInstance().getCurrInning(roomId);
			int status = PlayerCardsMgr.getInstance().getPhaseStatus(roomId);
			System.out.println("Room("+roomId+") round "+inning+" result:");
			Map<Integer, Integer> lastResult = totalMap.get(roomId);
			Map<Integer, Float> lastDrawMap = totalDrawMap.get(roomId);
			boolean coinroom = GameRoomMgr.getInstance().isCoinRoom(roomId);
			int percent = GameRoomMgr.getInstance().getDrawPercent(roomId);
			int ownerId = GameRoomMgr.getInstance().getOwner(roomId);
			for (Integer userId : currResult.keySet()) {
				int score = currResult.get(userId);
				float draw = 0;
				if((coinroom || !coinroom && userId != ownerId) && score>0){//房卡模式房主不抽水
					draw = (float)score/100 * percent;
				}
				sigleDrawMap.put(userId, draw);
				
				int total = 0;
				float totalDraw = 0;
				if (!lastResult.containsKey(userId)) {
					total = score;
					totalDraw = draw;
				} else {
					total = lastResult.get(userId) + score;
					totalDraw = lastDrawMap.get(userId) + draw;
				}
				lastResult.put(userId, total);
				lastDrawMap.put(userId, totalDraw);
				int seatId = GameRoomMgr.getInstance().getSeatId(roomId, userId);
				if (status == DealingHander.PHASE1) {
					TRoomInnings.updateByCriteria(TRoomInnings.valueList(TRoomInnings.AttrMIDDLE_SCORE.set(score),
							TRoomInnings.AttrMIDDLE_DRAW.set(draw),
							TRoomInnings.AttrMIDDLE_TOTAL.set(total)), 
							TRoomInnings.AttrROOM_ID.eq(roomId),
							TRoomInnings.AttrINNING.eq(inning),
							TRoomInnings.AttrSEAT_ID.eq(seatId),
	    					TRoomInnings.AttrSTATUS.eq(0));
				} else {
					TRoomInnings.updateByCriteria(TRoomInnings.valueList(TRoomInnings.AttrEND_SCORE.set(score),
							TRoomInnings.AttrEND_DRAW.set(draw),
							TRoomInnings.AttrEND_TOTAL.set(total), 
							TRoomInnings.AttrEND_TIME.set(new Date())), 
							TRoomInnings.AttrROOM_ID.eq(roomId),
							TRoomInnings.AttrINNING.eq(inning),
							TRoomInnings.AttrSEAT_ID.eq(seatId),
	    					TRoomInnings.AttrSTATUS.eq(0));
				}
				System.out.println("User("+userId+") score curr="+score+",total="+total);
			}
		}
	}
	
	// 获取单局输赢分
	public int onGetSingleResult(int roomId, int userId) {
		synchronized (GameResultMgr.class) {
			if (!singleMap.containsKey(roomId)) {
				return 0;
			}
			Map<Integer, Integer> result = singleMap.get(roomId);
			if (!result.containsKey(userId)) {
				return 0;
			}
			return result.get(userId);
		}
	}
	// 获取单局抽水
	public float getSingleDraw(int roomId, int userId) {
		synchronized (GameResultMgr.class) {
			if (!drawMap.containsKey(roomId)) {
				return 0;
			}
			Map<Integer, Float> draw = drawMap.get(roomId);
			if (!draw.containsKey(userId)) {
				return 0;
			}
			return draw.get(userId);
		}
	}
	
	// 获取总分
	public int onGetTotalResult(int roomId, int userId) {
		synchronized (GameResultMgr.class) {
			if (!totalMap.containsKey(roomId)) {
				return 0;
			} 
			Map<Integer, Integer> result = totalMap.get(roomId);
			if (!result.containsKey(userId)) {
				return 0;
			}
			return result.get(userId);
		}
	}
	
	// 获取总抽水
	public float getTotalDraw(int roomId, int userId) {
		synchronized (GameResultMgr.class) {
			if (!totalDrawMap.containsKey(roomId)) {
				return 0;
			} 
			Map<Integer, Float> result = totalDrawMap.get(roomId);
			if (!result.containsKey(userId)) {
				return 0;
			}
			return result.get(userId);
		}
	}
	
	// 获取当前房间所有用户总分
	public Map<Integer, Integer> onGetRoomTotalResult(int roomID) {
		synchronized (GameResultMgr.class) {
			if (!totalMap.containsKey(roomID)) {
				return new HashMap<Integer, Integer>();
			}
			return totalMap.get(roomID);
		}
	}
	
	// 获取房间内当局赢家
	public int getWinnerId(int roomID) {
		synchronized (GameResultMgr.class) {
			if (!singleMap.containsKey(roomID)) {
				return -1;
			}
			int maxScore = 0;
			int winnerId = -1;//平局，所有玩家分数都为0时，返回-1
			Map<Integer, Integer> result = singleMap.get(roomID);
			for (Entry<Integer, Integer> entry : result.entrySet()){
				int userId = entry.getKey();
				int score = entry.getValue();
				if (maxScore < score) {
					maxScore = score;
					winnerId = userId;
				}
	        }
			
			return winnerId;
		}
	}
}
