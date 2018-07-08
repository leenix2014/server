package netty.GameEvaluation;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class BullCardsLogic {
	
	public class CardInfo{
		int round;		// 花色，0=方块 1=梅花 2=红桃 3=黑桃
		int value;		// 牌面数字
		int weight;		// 
		int bak;		// 备用字段
	};
	
	private int[] cards = {
		0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D,
	    0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D,
	    0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D,
	    0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D,
	};
	
	private int dealingNum = 0;
	
	public static int getCardTypeMul(int cardType){
		int multiple;
		if (cardType == BullCardsConstantInterface.NiuFiveDukes || cardType == BullCardsConstantInterface.ThreeAce) {
			multiple = 5;
		}
		else if (cardType == BullCardsConstantInterface.NiuBullOfSpades || cardType == BullCardsConstantInterface.ThreeSame) {
			multiple = 4;
		}
		else if (cardType >= BullCardsConstantInterface.NiuPairOfKing_2 && cardType <= BullCardsConstantInterface.NiuPairOfAces || cardType == BullCardsConstantInterface.ThreeDukes) {
			multiple = 3;
		}
		else if (cardType == BullCardsConstantInterface.NiuNiu || cardType >= 9 && cardType <= 10) {
			multiple = 2;
		}
		else {
			multiple = 1;
		}
		return multiple;
	}
	
	// 交换函数，洗牌时用
	private void swap(int i, int j){
		if (i != j){
			int tmp = cards[i];
			cards[i] = cards[j];
			cards[j] = tmp;
		}
	}
	
	// 洗牌
	public void shuffle(){
		dealingNum = 0;
		Random generatorActor = new SecureRandom();
		for (int i = cards.length - 1; i > 0; --i) {
			int actor = generatorActor.nextInt(101);
			int idx = (int)Math.ceil(actor / 100.0 * i);
			swap(i, idx);
		}
	}
	
	// 发牌
	public Vector<Integer> dealing(int num) {
		Vector<Integer> result = new Vector<Integer>();
		for (int i = dealingNum; i < dealingNum + num; i++) {
			result.add(cards[i]);
		}
		dealingNum += num;
		return result;
	}
	
	// 获取所有牌牌面信息
	protected List<CardInfo> getCardFaces(List<Integer> handCard) {
		List<CardInfo> cards = new Vector<CardInfo>();
		
		for (int i = 0; i < handCard.size(); ++i) {
			cards.add(getCardFace(handCard.get(i)));
		}
		
		return cards;
	}
	
	// 获取一张牌牌面信息
	private CardInfo getCardFace(int card){
		int round = (card & 0xF0) >> 4;
		int value = card & 0x0F;
		CardInfo cardInfo = new CardInfo();
		cardInfo.round = round;
		cardInfo.value = value;
		cardInfo.weight = value > 10 ? 10 : value;
		cardInfo.bak = cardInfo.weight;
		return cardInfo;
	}
	
	// 是否五爵士
	protected boolean isAllDukes(List<CardInfo> cards){
		for (int i = 0; i < cards.size(); ++i) {
			if (cards.get(i).value <= 10) {
				return false;
			}
		}
		return true;
	}
	
	// 全是3和6
	private boolean isAllThreeAndSix(List<CardInfo> cards){
		for (int i = 0; i < cards.size(); ++i) {
			if ((cards.get(i).value != 3) && (cards.get(i).value != 6)){
				return false;
			}
		}
		return true;
	}
	
	// 
	private static int BitCount4(int n) 
	{ 
	    n = (n & 0x55555555) + ((n >>1) & 0x55555555); 
	    n = (n & 0x33333333) + ((n >>2) & 0x33333333); 
	    n = (n & 0x0f0f0f0f) + ((n >>4) & 0x0f0f0f0f); 
	    n = (n & 0x00ff00ff) + ((n >>8) & 0x00ff00ff); 
	    n = (n & 0x0000ffff) + ((n >>16) & 0x0000ffff); 

	    return n ; 
	}
	
	public static void main(String[] args){
		Vector<Vector<Integer>> combines = onGetAllCombine();
		for(Vector<Integer> combine : combines){
			System.out.print("Vecort<");
			for(Integer value : combine){
				System.out.print(value+",");
			}
			System.out.println(">");
		}
	}
	
	// 获取所有的组合可能
	private static Vector<Vector<Integer>> onGetAllCombine(){
		Vector<Vector<Integer>> arr = new Vector<Vector<Integer>>();
		for (int i = 0; i < 31; i++) {
			if (BitCount4(i) == 3){
				Vector<Integer> tmp = new Vector<Integer>();
				int tar = 0x10;
				while (tar != 0) {
					int result = (tar & i) == 0 ? 0 : 1;
					tmp.add(result);
					tar >>= 1;
				}
				arr.add(tmp);
			}
		}
		
		return arr;
	}
	
	// 获取忽略3、6互转时的牌型
	private int getCardTypeWithout36(List<CardInfo> cards, Vector<Integer> comb){
		Vector<Integer> lastIdxVector = new Vector<Integer>();
		int multiple = 0;
		int remainder = 0;
		for (int i = 0; i < cards.size(); i++) {
			if (comb.get(i) == 1) {
				multiple += cards.get(i).weight;
			} else {
				remainder += cards.get(i).weight;
				lastIdxVector.add(i);
			}
		}
		
		if (multiple % 10 != 0){
			return BullCardsConstantInterface.NoNiu;
		}
		if ((cards.get(lastIdxVector.get(0)).value == 1 && 
				cards.get(lastIdxVector.get(0)).round == 3 &&
				cards.get(lastIdxVector.get(1)).value > 10) ||
		    (cards.get(lastIdxVector.get(1)).value == 1 && 
				cards.get(lastIdxVector.get(1)).round == 3 &&
				cards.get(lastIdxVector.get(0)).value > 10)){
			return BullCardsConstantInterface.NiuBullOfSpades;
		}
		if ((cards.get(lastIdxVector.get(0)).value == cards.get(lastIdxVector.get(1)).value) &&
					(cards.get(lastIdxVector.get(0)).value == 1)){
			return BullCardsConstantInterface.NiuPairOfAces;
		}
		if ((cards.get(lastIdxVector.get(0)).value == cards.get(lastIdxVector.get(1)).value) &&
				(cards.get(lastIdxVector.get(0)).weight == cards.get(lastIdxVector.get(0)).bak) &&
				(cards.get(lastIdxVector.get(1)).weight == cards.get(lastIdxVector.get(1)).bak)){
			return BullCardsConstantInterface.NiuPairOfKing_2 + (cards.get(lastIdxVector.get(0)).value - 2);
		}
		int result = remainder % 10;
		if (result == 0) {
			return BullCardsConstantInterface.NiuNiu;
		} else {
			return BullCardsConstantInterface.NoNiu + result;
		}
	}
	
	// 单个组合获取牌型
	private int getSingleCardType(List<CardInfo> cards, Vector<Integer> comb){
		
		Vector<Integer> idxVector = new Vector<Integer>();
		int switchNum = 0;
		for (int i = 0; i < cards.size(); ++i) {
			if ((cards.get(i).value == 3) || (cards.get(i).value == 6)){
				++switchNum;
				idxVector.add(i);
			}
		}
		
		if (switchNum == 0){
			return getCardTypeWithout36(cards, comb);
		}
		else{
			int maxType = BullCardsConstantInterface.NoNiu;
			List<CardInfo> cardsBak = new ArrayList<CardInfo>();
			cardsBak.addAll(cards);
			int count = (int)Math.pow(2, switchNum) - 1;
			for (int i = 0; i <= count; i++) {
				int tar = 0x01;
				for (int j = 0; j < idxVector.size(); j++) {
					if ((tar & i) == 0 ) {
						cardsBak.get(idxVector.get(j)).weight = 3;
					}
					else {
						cardsBak.get(idxVector.get(j)).weight = 6;
					}
					tar <<= 1;
				}
				int cardsType = getCardTypeWithout36(cardsBak, comb);
				if (cardsType > maxType){
					maxType = cardsType;
				}
			}
			return maxType;
		}
	}
	
	public int getBestCardType(List<Integer> handcards){
		int bestCardType = 0;
		for(Integer type : getBestCard(handcards).keySet()){
			bestCardType = type;
		}
		return bestCardType;
	}
	
	// 计算最好牌型
	// @return Map<cardType, Vector<bestCards>
	public Map<Integer, List<Integer>> getBestCard(List<Integer> handcards){
		List<CardInfo> cards = getCardFaces(handcards);
		Map<Integer, List<Integer>> bestCard = new HashMap<Integer, List<Integer>>();
		if (isAllDukes(cards)){
			bestCard.put(BullCardsConstantInterface.NiuFiveDukes, handcards);
			return bestCard;
		}
		else if (isAllThreeAndSix(cards)) {
			bestCard.put(BullCardsConstantInterface.NoNiu, handcards);
			return bestCard;
		}
		
		return getMaxCardType(handcards);
	}
	
	// 多个组合获取最好牌型
	private Map<Integer, List<Integer>> getMaxCardType(List<Integer> handcards){
		List<CardInfo> cards = getCardFaces(handcards);
		Map<Integer, List<Integer>> bestCard = new HashMap<Integer, List<Integer>>();
		int maxType = BullCardsConstantInterface.NoNiu;
		List<Integer> maxCard = handcards;
		Vector<Vector<Integer>> combines = onGetAllCombine();
		for (Vector<Integer> combine : combines) {
			int cardsType = getSingleCardType(cards, combine);
			if (cardsType > maxType){
				maxType = cardsType;
				
				maxCard = new Vector<Integer>();
				//记录拼成牛的牌
				for(int i=0;i<combine.size();i++){
					if(combine.get(i)==1){
						maxCard.add(handcards.get(i));
					}
				}
				//记录剩下两张牌
				for(int i=0;i<combine.size();i++){
					if(combine.get(i)==0){
						maxCard.add(handcards.get(i));
					}
				}
			}
		}
		bestCard.put(maxType, maxCard);
		return bestCard;
	}
	
	// 判断是牛几
	private int onCalculatorNiuType(int value) {
		int result = value % 10;
		if (result == 0) {
			return BullCardsConstantInterface.NiuNiu;
		} 
		else {
			return BullCardsConstantInterface.NoNiu + result;
		}
	}
	
	// 判断前三张牌是否能组成牛
	private boolean onGetThreeCardsType(CardInfo[] cards) {
		int sum = 0;
		int switchNum = 0;
		for (int i = 0; i < cards.length; ++i) {
			if ((cards[i].value == 3) || (cards[i].value == 6)){
				++switchNum;
			}
			else {
				sum += cards[i].weight;
			}
		}
		
		switch (switchNum) {
		case 0:
			if (sum % 10 == 0) {
				return true;
			}
			else {
				return false;
			}
			
		case 1:
			if (sum == 7 || sum == 17 || sum == 4 || sum == 14) {
				return true;
			}
			else {
				return false;
			}
			
		case 2:
			if (sum == 1 || sum == 4 || sum == 8) {
				return true;
			}
			else {
				return false;
			}
			
		case 3:
			return false;
			
		default:
			return false;
		}
	}
	
	// 获取剩余两张牌的牌型
	private int onGetTwoCardsType(CardInfo[] cards) {
		
		int sum = 0;
		int switchNum = 0;
		for (int i = 0; i < cards.length; ++i) {
			if ((cards[i].value == 3) || (cards[i].value == 6)){
				++switchNum;
			}
			else {
				sum += cards[i].weight;
			}
		}
		
		if ((cards[0].value == 1 && cards[0].round == 3 && cards[1].value > 10) ||
		    (cards[1].value == 1 && cards[1].round == 3 && cards[0].value > 10)){
			return BullCardsConstantInterface.NiuBullOfSpades;
		}
		else if ((cards[0].value == cards[1].value) && (cards[0].value == 1)){
			return BullCardsConstantInterface.NiuPairOfAces;
		}
		else if ((cards[0].value == cards[1].value) && (cards[0].weight == cards[0].bak) && (cards[1].weight == cards[1].bak)){
			return BullCardsConstantInterface.NiuPairOfKing_2 + (cards[0].value - 2);
		}
		else {
			if (switchNum == 1) {
				int cardsType = BullCardsConstantInterface.NoNiu;
				int[] tarArr = new int[]{sum + 3, sum + 6};
				for (int i = 0; i < tarArr.length; i++) {
					int curType = onCalculatorNiuType(tarArr[i]);
					if (curType > cardsType) {
						cardsType = curType;
					}
				}
				return cardsType;
			}
			else if (switchNum == 2) {
				return BullCardsConstantInterface.NiuNine;
			}
			else {
				return onCalculatorNiuType(sum);
			}
		}
	}
	
	// 获取用户自己组合的牌型
	public int getCustomerCardType(List<Integer> handcards){
		List<CardInfo> cards = getCardFaces(handcards);
		if (isAllDukes(cards)){
			return BullCardsConstantInterface.NiuFiveDukes;
		}
		else if (isAllThreeAndSix(cards)) {
			return BullCardsConstantInterface.NoNiu;
		}
		else {
			if (onGetThreeCardsType(new CardInfo[]{cards.get(0), cards.get(1), cards.get(2)})) {
				return onGetTwoCardsType(new CardInfo[]{cards.get(3), cards.get(4)});
			}
			else {
				return BullCardsConstantInterface.NoNiu;
			}
		}
	}
}
