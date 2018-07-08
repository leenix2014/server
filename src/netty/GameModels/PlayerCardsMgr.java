package netty.GameModels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.mozat.morange.dbcache.tables.TRoomInnings;

import netty.GameEvaluation.BullCardsConstantInterface;
import netty.GameEvaluation.BullCardsLogic;
import netty.GameEvaluation.ThreeCardsLogic;
import netty.base.DealingHander;

public class PlayerCardsMgr {
	
	volatile private static PlayerCardsMgr instance = null;
	
    private PlayerCardsMgr(){
    }

    public static PlayerCardsMgr getInstance() {

        if(instance == null){
            synchronized (PlayerCardsMgr.class) {
                if(instance == null){
                    instance = new PlayerCardsMgr();
                }
            }
        }

        return instance;
    }
    
    private static final int PLAYER_CARDS_NUM = 5;
    
    // Map<roomid, Map<userId, List<HandCard>> >
    // 玩家手牌
    private Map<Integer, Map<Integer, List<Integer>> > cardsMap = new HashMap<Integer, Map<Integer, List<Integer>>>();
    
    // Map<roomid, Map<userId, cardType> >
    // 玩家手牌牌型
    private Map<Integer, Map<Integer, Integer> > cardTypeMap = new HashMap<Integer, Map<Integer, Integer>>();
    
    // Map<roomid, count>
    // 摊牌人数
    private Map<Integer, Integer> showHandCount = new HashMap<Integer, Integer>();
    
    // Map<roomid, status>
    // 三五张当前状态
    private Map<Integer, Integer> phaseStatus = new HashMap<Integer, Integer>();
    
    // 游戏开局发牌
    // @return Map<userId, Vector<HandCard>>
    public Map<Integer, List<Integer> > onPlayerDealing(int roomID) {
    	Map<Integer, List<Integer> > playerCards = new HashMap<Integer, List<Integer>>();
    	
    	Set<Integer> userIds = GameRoomMgr.getInstance().getRoomUsers(roomID);
    	BullCardsLogic logic = new BullCardsLogic();
    	logic.shuffle();
    	int inning = GameInningMgr.getInstance().getCurrInning(roomID);
    	for (int userId : userIds) {
    		Vector<Integer> cards = logic.dealing(PLAYER_CARDS_NUM);
    		playerCards.put(userId, cards);
    		StringBuffer cardStr = new StringBuffer();
    		for(int card : cards){
    			cardStr.append(card + ",");
    		}
    		cardStr.deleteCharAt(cardStr.length()-1);
    		TRoomInnings.updateByCriteria(TRoomInnings.valueList(TRoomInnings.AttrCARDS.set(cardStr.toString())), 
					TRoomInnings.AttrROOM_ID.eq(roomID),
					TRoomInnings.AttrINNING.eq(inning),
					TRoomInnings.AttrSEAT_ID.eq(GameRoomMgr.getInstance().getSeatId(roomID, userId)),
					TRoomInnings.AttrSTATUS.eq(0));
		}
    	
    	synchronized (PlayerCardsMgr.class) {
    		cardsMap.put(roomID, playerCards);
    		
    		Map<Integer, Integer> playerCardTypes = new HashMap<Integer, Integer>();
			cardTypeMap.put(roomID, playerCardTypes);
    		
    		showHandCount.put(roomID, 0);
    	}
    	
    	return playerCards;
    }
    
    // 玩家摊牌上传手牌后计算牌型
    public void onUpdatePlayerCards(int roomID, int userId, Vector<Integer> handcards) {
    	
    	int gameType = GameRoomMgr.getInstance().getRoomGameType(roomID);
    	int type = -1;
    	if (gameType == BullCardsConstantInterface.BullGame) {
    		BullCardsLogic logic = new BullCardsLogic();
    		type = logic.getCustomerCardType(handcards);
    	}
    	else if (gameType == BullCardsConstantInterface.ThreeCardGame) {
    		ThreeCardsLogic logic = new ThreeCardsLogic();
    		if (handcards.size() == 3) {
    			type = logic.getThreeCardType(handcards);
			} 
    		else if (handcards.size() == 5) {
    			type = logic.getCustomerCardType(handcards);
			}
    		else {
    			System.out.println("onUpdatePlayerCards gameType is wrong. handcards len = " + handcards.size());
			}
    	}
    	else {
    		System.out.println("onUpdatePlayerCards gameType is wrong. gameType is " + gameType);
		}
		
    	synchronized (PlayerCardsMgr.class) {
    		if (!cardsMap.containsKey(roomID)) {
    			return;
    		}
    		Map<Integer, List<Integer> > playerCards = cardsMap.get(roomID);
	    	if (!playerCards.containsKey(userId)) {
	    		return;
			}
	    	int inning = GameInningMgr.getInstance().getCurrInning(roomID);
	    	if (handcards.size() == 5) {
    			playerCards.put(userId, handcards);
    			StringBuffer cardStr = new StringBuffer();
        		for(int card : handcards){
        			cardStr.append(card + ",");
        		}
        		cardStr.deleteCharAt(cardStr.length()-1);
        		TRoomInnings.updateByCriteria(TRoomInnings.valueList(TRoomInnings.AttrSHOW_HAND_CARDS.set(cardStr.toString())), 
    					TRoomInnings.AttrROOM_ID.eq(roomID),
    					TRoomInnings.AttrINNING.eq(inning),
    					TRoomInnings.AttrSEAT_ID.eq(GameRoomMgr.getInstance().getSeatId(roomID, userId)),
    					TRoomInnings.AttrSTATUS.eq(0));
    		}
    		if (showHandCount.containsKey(roomID)) {
    			showHandCount.put(roomID, 1 + showHandCount.get(roomID));
    		}
    		else {
    			showHandCount.put(roomID, 1);
			}
			
			if (cardTypeMap.containsKey(roomID)) {
				Map<Integer, Integer> cardTypes = cardTypeMap.get(roomID);
				cardTypes.put(userId, type);
				if(handcards.size() == 3){
					TRoomInnings.updateByCriteria(TRoomInnings.valueList(TRoomInnings.AttrMIDDLE_CARD_TYPE.set(type+"")), 
	    					TRoomInnings.AttrROOM_ID.eq(roomID),
	    					TRoomInnings.AttrINNING.eq(inning),
	    					TRoomInnings.AttrSEAT_ID.eq(GameRoomMgr.getInstance().getSeatId(roomID, userId)),
	    					TRoomInnings.AttrSTATUS.eq(0));
				} else {
					TRoomInnings.updateByCriteria(TRoomInnings.valueList(TRoomInnings.AttrCARD_TYPE.set(type+"")), 
	    					TRoomInnings.AttrROOM_ID.eq(roomID),
	    					TRoomInnings.AttrINNING.eq(inning),
	    					TRoomInnings.AttrSEAT_ID.eq(GameRoomMgr.getInstance().getSeatId(roomID, userId)),
	    					TRoomInnings.AttrSTATUS.eq(0));
				}
			}
    	}
    }
    
    public Map<Integer, List<Integer>> getRoomHandCard(int roomID){
    	synchronized (PlayerCardsMgr.class) {
	    	if(!cardsMap.containsKey(roomID)){
	    		return new HashMap<Integer, List<Integer>>();
	    	}
	    	return cardsMap.get(roomID);
    	}
    }
    
    public List<Integer> getPlayerHandCard(int roomId, int userId){
    	Map<Integer, List<Integer>> roomCards = getRoomHandCard(roomId);
    	synchronized (PlayerCardsMgr.class) {
    		if(!roomCards.containsKey(userId)){
    			return new Vector<Integer>();
    		}
    		return roomCards.get(userId);
    	}
    }
    
    // 记录最好牌型
    public void calBestCardType(int roomID) {
    	int gameType = GameRoomMgr.getInstance().getRoomGameType(roomID);
    	int inning = GameInningMgr.getInstance().getCurrInning(roomID);
    	synchronized (PlayerCardsMgr.class) {
    		if (!cardsMap.containsKey(roomID) || !cardTypeMap.containsKey(roomID)) {
    			return;
    		}
    		Map<Integer, List<Integer>> handCards = cardsMap.get(roomID);
			Map<Integer, Integer> cardTypes = cardTypeMap.get(roomID);
			for (Integer userId : handCards.keySet()) {
				if (cardTypes.containsKey(userId)) {
        			continue;//Already Show Hand
				}
				int type = -1;
    			if (gameType == BullCardsConstantInterface.BullGame) {
    				BullCardsLogic logic = new BullCardsLogic();
    				Map<Integer, List<Integer>> bestCardType = logic.getBestCard(handCards.get(userId));
    				for(Map.Entry<Integer, List<Integer>> entry : bestCardType.entrySet()){
    					type = entry.getKey();
    					List<Integer> bestCard = entry.getValue();
    					handCards.put(userId, bestCard);
    				}
    				TRoomInnings.updateByCriteria(TRoomInnings.valueList(TRoomInnings.AttrCARD_TYPE.set(type+"")), 
	    					TRoomInnings.AttrROOM_ID.eq(roomID),
	    					TRoomInnings.AttrINNING.eq(inning),
	    					TRoomInnings.AttrSEAT_ID.eq(GameRoomMgr.getInstance().getSeatId(roomID, userId)),
	    					TRoomInnings.AttrSTATUS.eq(0));
				} 
    			else if (gameType == BullCardsConstantInterface.ThreeCardGame) {
    				ThreeCardsLogic logic = new ThreeCardsLogic();
    				int status = PlayerCardsMgr.getInstance().getPhaseStatus(roomID);
    				if (status == DealingHander.PHASE1) {
    					List<Integer> playerHandCard = handCards.get(userId);
    					List<Integer> firstThreeCard = new Vector<Integer>();
    					for (int i = 0; i < playerHandCard.size() - 2; ++i) {
    						firstThreeCard.add(playerHandCard.get(i));
						}
    					type = logic.getThreeCardType(firstThreeCard);
    					TRoomInnings.updateByCriteria(TRoomInnings.valueList(TRoomInnings.AttrMIDDLE_CARD_TYPE.set(type+"")), 
    	    					TRoomInnings.AttrROOM_ID.eq(roomID),
    	    					TRoomInnings.AttrINNING.eq(inning),
    	    					TRoomInnings.AttrSEAT_ID.eq(GameRoomMgr.getInstance().getSeatId(roomID, userId)),
    	    					TRoomInnings.AttrSTATUS.eq(0));
    				}
    				else if (status == DealingHander.PHASE2) {
    					Map<Integer, List<Integer>> bestCardType = logic.getBestCard(handCards.get(userId));
        				for(Map.Entry<Integer, List<Integer>> entry : bestCardType.entrySet()){
        					type = entry.getKey();
        					List<Integer> bestCard = entry.getValue();
        					handCards.put(userId, bestCard);
        				}
        				TRoomInnings.updateByCriteria(TRoomInnings.valueList(TRoomInnings.AttrCARD_TYPE.set(type+"")), 
    	    					TRoomInnings.AttrROOM_ID.eq(roomID),
    	    					TRoomInnings.AttrINNING.eq(inning),
    	    					TRoomInnings.AttrSEAT_ID.eq(GameRoomMgr.getInstance().getSeatId(roomID, userId)),
    	    					TRoomInnings.AttrSTATUS.eq(0));
    				}
				}
    			else {
    				System.out.println("onCalculateBestCardType gameType is wrong. gameType is = " + gameType);
				}
					
    			cardTypes.put(userId, type);
			}
    	}
    }
    
    // 根据座位号获取牌型
    public int getCardType(int roomID, int userId) {
    	synchronized (PlayerCardsMgr.class) {
    		if (!cardTypeMap.containsKey(roomID)) {
    			return -1;
			}
    		Map<Integer, Integer> cardTypes = cardTypeMap.get(roomID);
			if (!cardTypes.containsKey(userId)) {
				return -1;
			}
			return cardTypes.get(userId);
    	}
    }
    
    public Map<Integer, Integer> getCardTypeMap(int roomId){
    	synchronized (PlayerCardsMgr.class) {
	    	Map<Integer, Integer> map = new HashMap<>();
	    	Map<Integer, Integer> cardType = cardTypeMap.get(roomId);
	    	if(cardType != null){
	    		map.putAll(cardType);
	    	}
	    	return map;
    	}
    }
    
    // return Map<userId, cardType>
    public Map<Integer, Integer> getRoomCardTypes(int roomID) {
    	synchronized (PlayerCardsMgr.class) {
    		if (cardTypeMap.containsKey(roomID)) {
    			return cardTypeMap.get(roomID);
			} 
    		else {
    			return new HashMap<Integer, Integer>();
			}
    	}
    }
    
    // 是否所有玩家都已摊牌
    public boolean isAllPlayerShowHand(int roomID) {
    	synchronized (PlayerCardsMgr.class) {
    		Integer count = showHandCount.get(roomID);
    		if(count == null){
    			return false;
    		}
    		int userCount = GameRoomMgr.getInstance().getRoomUserCount(roomID);
    		int offlineCount = GameRoomMgr.getInstance().getOfflineCount(roomID);
    		return count >= userCount - offlineCount;
    	}
    }
    
    // 更新三五张当前状态
    public void setPhaseStatus(int roomID, int status) {
    	synchronized (PlayerCardsMgr.class) {
    		phaseStatus.put(roomID, status);
    	}
    }
    
    // 获取三五张当前状态
    public int getPhaseStatus(int roomID) {
    	synchronized (PlayerCardsMgr.class) {
    		if (!phaseStatus.containsKey(roomID)) {
    			return DealingHander.PHASE2;
			} 
    		return phaseStatus.get(roomID);
    	}
    }
    
    // 清空手牌信息
    public void clearHandCard(int roomID) {
    	synchronized (PlayerCardsMgr.class) {
    		cardsMap.remove(roomID);
			cardTypeMap.remove(roomID);
			showHandCount.remove(roomID);
    	}
    }
    
    // 三五张第一回合结束时，清空牌型信息，避免误认为已show hand
    public void clearCardType(int roomID) {
    	synchronized (PlayerCardsMgr.class) {
    		Map<Integer, Integer> typeInfo = new HashMap<Integer, Integer>();
    		cardTypeMap.put(roomID, typeInfo);
    		showHandCount.put(roomID, 0);
    	}
    }
}
