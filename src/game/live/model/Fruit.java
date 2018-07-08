package game.live.model;

import java.util.HashMap;
import java.util.Map;

import netty.util.WeightRandom;
import protocols.live.betfruit.fruit_bet;

public class Fruit {
	private static WeightRandom<Integer> random = new WeightRandom<>();
	static {
		Map<Integer, Integer> poss = new HashMap<>();
		poss.put(1, 36);   //Orange x10
		poss.put(2, 18);  //Bell x20
		poss.put(3, 7);  //Bar x50
		poss.put(4, 3);  //Bar x120
		poss.put(5, 72);  //Apple x5
		poss.put(6, 120);  //Apple x3
		poss.put(7, 24);  //Lemon x15
		poss.put(8, 18);  //Melon x20
		poss.put(9, 120);  //Melon x3
		poss.put(10, 0); //lucky
		poss.put(11, 72); //Apple x5
		poss.put(12, 120); //Orange x3
		poss.put(13, 36); //Orange x10
		poss.put(14, 18); //Bell x20
		poss.put(15, 120); //Seven x3
		poss.put(16, 9); //Seven x40
		poss.put(17, 72); //Apple x5
		poss.put(18, 120); //Lemon x3
		poss.put(19, 20); //Lemon x15
		poss.put(20, 120); //Star x3
		poss.put(21, 12); //Star x30
		poss.put(22, 0); //lucky
		poss.put(23, 72); //Apple x5
		poss.put(24, 120); //Bell x3
		random.setWeightedObjects(poss);
	}
	
	private fruit_bet bet;
	private int result;
	private int reward;
	
	public Fruit bet(fruit_bet bet){
		this.bet = bet;
		return this;
	}
	
	public void openPrize(){
		result = random.next();
		if(result == 1){
			reward = bet.getOrange() * 10;
		} else if(result == 2){
			reward = bet.getBell() * 20;
		} else if(result == 3){
			reward = bet.getBar() * 50;
		} else if(result == 4){
			reward = bet.getBar() * 120;
		} else if(result == 5){
			reward = bet.getApple() * 5;
		} else if(result == 6){
			reward = bet.getApple() * 3;
		} else if(result == 7){
			reward = bet.getLemon() * 15;
		} else if(result == 8){
			reward = bet.getMelon() * 20;
		} else if(result == 9){
			reward = bet.getMelon() * 3;
		} else if(result == 10){
			//reward = bet.getApple() * 5; // lucky
		} else if(result == 11){
			reward = bet.getApple() * 5;
		} else if(result == 12){
			reward = bet.getOrange() * 3;
		} else if(result == 13){
			reward = bet.getOrange() * 10;
		} else if(result == 14){
			reward = bet.getBell() * 20;
		} else if(result == 15){
			reward = bet.getSeven() * 3;
		} else if(result == 16){
			reward = bet.getSeven() * 40;
		} else if(result == 17){
			reward = bet.getApple() * 5;
		} else if(result == 18){
			reward = bet.getLemon() * 3;
		} else if(result == 19){
			reward = bet.getLemon() * 15;
		} else if(result == 20){
			reward = bet.getStar() * 3;
		} else if(result == 21){
			reward = bet.getStar() * 30;
		} else if(result == 22){
			//reward = bet.getApple() * 5;//lucky
		} else if(result == 23){
			reward = bet.getApple() * 5;
		} else if(result == 24){
			reward = bet.getBell() * 3;
		}
	}
	
	public int getReward(){
		return reward;
	}

	public int getResult() {
		return result;
	}
}
