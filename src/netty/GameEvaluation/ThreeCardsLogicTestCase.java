package netty.GameEvaluation;

import java.util.ArrayList;
import java.util.List;

public class ThreeCardsLogicTestCase {

	private ThreeCardsLogic logic = new ThreeCardsLogic();
	
	public ThreeCardsLogicTestCase(){
		
	}
	
	private void ThreeCardsTestCase(int[] handCard, int cardType){
		List<Integer> cards = new ArrayList<Integer>();
		for (int i = 0; i < handCard.length; i++) {
			cards.add(handCard[i]);
		}
		assert logic.getThreeCardType(cards) == cardType;
		/*
		int curType = logic.onGetThreeCardsType(cards);
		System.out.println("=========== curType = " + curType);
		assert curType == cardType;
		*/
	}
	
	public void onThreeCardsTypeTestCase(){
		
		System.out.println("onThreeCardsTypeTestCase start.");
		
		ThreeCardsTestCase(new int[]{1, 33, 49}, BullCardsConstantInterface.ThreeAce);
		
		ThreeCardsTestCase(new int[]{17, 33, 49}, BullCardsConstantInterface.ThreeAce);
		
		ThreeCardsTestCase(new int[]{17, 1, 49}, BullCardsConstantInterface.ThreeAce);
		
		ThreeCardsTestCase(new int[]{6, 22, 38}, BullCardsConstantInterface.ThreeSame);
		
		ThreeCardsTestCase(new int[]{10, 26, 58}, BullCardsConstantInterface.ThreeSame);
		
		ThreeCardsTestCase(new int[]{11, 12, 13}, BullCardsConstantInterface.ThreeDukes);
		
		ThreeCardsTestCase(new int[]{61, 12, 13}, BullCardsConstantInterface.ThreeDukes);
		
		ThreeCardsTestCase(new int[]{1, 2, 7}, 10);
		
		ThreeCardsTestCase(new int[]{1, 50, 39}, 10);
		
		System.out.println("onThreeCardsTypeTestCase pass.");
	}
	
}
