package game.coinroom.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.coinroom.util.MapUtil;
import netty.GameEvaluation.ThreeCardsLogic;
import netty.GameModels.UserMgr;

public class CompareLogic {
	private static Logger logger = LoggerFactory.getLogger(CompareLogic.class);
	//Input
	private int baseScore;
	private int banker;
	// Map<userId, bet>
	private Map<Integer, Integer> playerBet = new ConcurrentHashMap<Integer, Integer>();
	// Map<userId, cardType>
	private Map<Integer, Integer> playerCardType = new ConcurrentHashMap<Integer, Integer>();
	
	// Output
	// Map<userId, score>
	private Map<Integer, Integer> playerScore = new ConcurrentHashMap<Integer, Integer>();
	public Map<Integer, Integer> getPlayerScore(){
		return playerScore;
	}
	
	public CompareLogic(int baseScore, int banker, Map<Integer, Integer> playerBet, Map<Integer, Integer> playerCardType){
		this.baseScore = baseScore;
		this.banker = banker;
		this.playerBet = playerBet;
		this.playerCardType = playerCardType;
	}

	public void compareToEachOther(){
		Map<Integer, Integer> afterCoins = initPlayerCoin();
		//从小牌到大牌轮流做庄家比较，小的先输光
		Map<Integer, Integer> ascCardType = MapUtil.sortByValue(playerCardType, true);
		for(Map.Entry<Integer, Integer> entry:ascCardType.entrySet()){
			int playerId = entry.getKey();
			compareToBanker(playerId, afterCoins);
		}
		calScore(afterCoins);
	}
	
	public void compareToBanker(){
		Map<Integer, Integer> afterCoins = initPlayerCoin();
		compareToBanker(banker, afterCoins);
		calScore(afterCoins);
	}
	
	private void calScore(Map<Integer, Integer> afterCoins){
		Map<Integer, Integer> initCoins = initPlayerCoin();
		for(Map.Entry<Integer, Integer> entry : afterCoins.entrySet()){
			int playerId = entry.getKey();
			int afterCoin = entry.getValue();
			int initCoin = initCoins.get(playerId);
			int score = afterCoin - initCoin;
			playerScore.put(playerId, score);
		}
		comparedCouple.clear();
	}
	
	private Map<Integer, Integer> initPlayerCoin(){
		Map<Integer, Integer> playerCoin = new HashMap<Integer, Integer>();
		for(Integer playerId : playerCardType.keySet()) {
			int userCoin = UserMgr.getInstance().getUserCoin(playerId);
			playerCoin.put(playerId, userCoin);
		}
		return playerCoin;
	}
	
	private void compareToBanker(int bankerId, Map<Integer, Integer> playerCoin){
		// int bankerGrab = getGrab(bankerId);
		int bankerCardType = getCardType(bankerId);
		int bankerScore = 0;
		int bankerCoin = playerCoin.get(bankerId);
		// Map<userId, cardType>
		Map<Integer, Integer> ascCardType = MapUtil.sortByValue(playerCardType, true);
		Iterator<Entry<Integer, Integer>> iter = ascCardType.entrySet().iterator();
		// 庄家先赢钱,从小牌型开始赢
		while(iter.hasNext()){
			Map.Entry<Integer, Integer> entry = iter.next();
			int playerId = entry.getKey();
			if(compared(bankerId, playerId)){
				continue;
			}
			int cardType = entry.getValue();
			int bankerWin = 0;
			if(bankerCardType > cardType){
				// bankerWin = baseScore * bankerGrab * getBet(playerId) * ThreeCardsLogic.getCardTypeMul(bankerCardType);
				bankerWin = baseScore * getBet(playerId) * ThreeCardsLogic.getCardTypeMul(bankerCardType);
			} else if(cardType == bankerCardType){
				bankerWin = 0;
			} else {
				break;//庄家要输钱了，跳出循环
			}
			int userCoin = playerCoin.get(playerId);
			if(userCoin - bankerWin < 0){
				bankerWin = userCoin;
			}
			playerCoin.put(playerId, userCoin-bankerWin);//内存中扣款
			bankerScore += bankerWin;
			addCompared(bankerId, playerId);
			iter.remove();//已比对过的删除
		}
		Map<Integer, Integer> descCardType = MapUtil.sortByValue(ascCardType, false);
		// 庄家开始输钱，从大到小输
		for(Map.Entry<Integer, Integer> entry : descCardType.entrySet()){
			int playerId = entry.getKey();
			if(compared(bankerId, playerId)){
				continue;
			}
			int cardType = entry.getValue();
			int bankerLose = 0;
			if(bankerCardType >= cardType){
				logger.error("Logic error! Banker should not win!");
			} else {
				// bankerLose = baseScore * bankerGrab * getBet(playerId) * ThreeCardsLogic.getCardTypeMul(cardType);
				bankerLose = baseScore * getBet(playerId) * ThreeCardsLogic.getCardTypeMul(cardType);
			}
			if(bankerCoin - bankerLose < 0){
				bankerLose = bankerCoin;
				break;//庄家输完钱了，跳出循环
			}
			int userCoin = playerCoin.get(playerId);
			playerCoin.put(playerId, userCoin+bankerLose);//内存中扣款
			bankerScore -= bankerLose;
			addCompared(bankerId, playerId);
		}
		playerCoin.put(bankerId, bankerCoin+bankerScore);
	}
	
	private static class Couple{
		private int player1;
		private int player2;
		public Couple(int player1, int player2){
			this.player1 = player1;
			this.player2 = player2;
		}
		@Override
		public int hashCode() {
	        return player1 + player2;
		}
		@Override
		public boolean equals(Object anObject) {
			if (this == anObject) {
	            return true;
	        }
	        if (anObject instanceof Couple) {
	        	Couple other = (Couple)anObject;
	            if((this.player1 == other.player1 && this.player2 == other.player2)
	            		|| (this.player2 == other.player1 && this.player1 == other.player2)){
	            	return true;
	            }
	        }
	        return false;
		}
	}
	private Set<Couple> comparedCouple = new HashSet<>();
	
	private boolean compared(int player1, int player2){
		if(player1 == player2){
			return true;
		}
		return comparedCouple.contains(new Couple(player1, player2));
	}
	
	private void addCompared(int player1, int player2){
		comparedCouple.add(new Couple(player1, player2));
	}
	
	private int getBet(int userId){
		Integer bet = playerBet.get(userId);
		if(bet == null || bet <= 0){
			return 1;
		}
		return bet;
	}
	
	private int getCardType(int userId){
		Integer cardType = playerCardType.get(userId);
		if(cardType == null){
			return 0;
		}
		return cardType;
	}
}
