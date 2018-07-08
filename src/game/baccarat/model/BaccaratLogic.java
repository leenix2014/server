package game.baccarat.model;

import java.util.Collection;
import java.util.List;

import game.common.Poker;

public class BaccaratLogic {

	// 获取点数
	public static int getPoint(Collection<Integer> hands){
		int point = 0;
		for(Integer hand : hands){
			Poker card = Poker.getPoker(hand);
			point += card.getWeight();
		}
		return point%10;
	}
	
	public static boolean isSkyPoint(int point){
		return point == 8 || point == 9;
	}
	
	public static boolean isPair(List<Integer> hands){
		if(hands == null || hands.size() != 2){
			return false;
		}
		return Poker.getPoker(hands.get(0)).getPoint() == Poker.getPoker(hands.get(1)).getPoint();
	}
	
	public static boolean isPlayerMustNeedCard(int point){
		return point>=0 && point <=5;
	}
	
	public static boolean isBankerMustNeedCard(int point){
		return point>=0 && point <=2;
	}
}
