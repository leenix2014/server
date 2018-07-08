package netty.GameEvaluation;

import java.util.List;
import java.util.Map;

public class ThreeCardsLogic extends BullCardsLogic {

	public int getCardType(List<Integer> handcards){
		if(handcards.size() == 3){
			return this.getThreeCardType(handcards);
		} else {
			return this.getCustomerCardType(handcards);
		}
	}
	
	@Override
	public int getBestCardType(List<Integer> handcards){
		if(handcards.size() == 3){
			return this.getThreeCardType(handcards);
		} else {
			return super.getBestCardType(handcards);
		}
	}
	
	public List<Integer> getBest(List<Integer> handcards){
		if(handcards.size() == 3){
			return handcards;
		}
		Map<Integer, List<Integer>> bestCards = super.getBestCard(handcards);
		for(List<Integer> best : bestCards.values()){
			return best;
		}
		return handcards;
	}
	
	// 计算三五张第一阶段牌型
	public int getThreeCardType(List<Integer> handcards) {
		List<CardInfo> cards = getCardFaces(handcards);
		if (isAllSameCardValue(cards)) {
			if (cards.get(0).value == 1) {
				return BullCardsConstantInterface.ThreeAce;
			} else {
				return BullCardsConstantInterface.ThreeSame;
			}
		}
		else if (isAllDukes(cards)) {
			return BullCardsConstantInterface.ThreeDukes;
		}
		else {
			int sum = 0;
			for (int i = 0; i < cards.size(); i++) {
				sum += cards.get(i).weight;
			}
			int result = sum % 10;
			result = result == 0 ? 10 : result;
			return result;
		}
	}
	
	// 是否全是一样的牌
	private boolean isAllSameCardValue(List<CardInfo> cards) {
		for (int i = 0; i < cards.size() - 1; i++) {
			if (cards.get(i).value != cards.get(i + 1).value) {
				return false;
			}
		}
		
		return true;
	}
}
