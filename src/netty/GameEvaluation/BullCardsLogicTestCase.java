package netty.GameEvaluation;

import java.util.ArrayList;
import java.util.List;

public class BullCardsLogicTestCase {
	
	private BullCardsLogic logic = new BullCardsLogic();

	public BullCardsLogicTestCase() {
		
	}
	
	public static void main(String[] args){
		BullCardsLogicTestCase tester = new BullCardsLogicTestCase();
		tester.onBullCardsBestCardTypeTestCase();
		tester.onBullCardsSpecificCardTypeTestCase();
	}
	
	private void onSpecifiedCardTypeTestCase(int[] handCard, int cardType){
		List<Integer> cards = new ArrayList<Integer>();
		for (int i = 0; i < handCard.length; i++) {
			cards.add(handCard[i]);
		}
		assert logic.getCustomerCardType(cards) == cardType;
	}
	
	public void onBullCardsSpecificCardTypeTestCase(){
		
		System.out.println("onBullCardsSpecificCardTypeTestCase start.");
		
		onSpecifiedCardTypeTestCase(new int[]{3, 6, 8, 1, 1}, BullCardsConstantInterface.NiuPairOfAces);

		onSpecifiedCardTypeTestCase(new int[]{3, 6, 1, 8, 2}, BullCardsConstantInterface.NiuNiu);

		onSpecifiedCardTypeTestCase(new int[]{3, 6, 8, 1, 3}, BullCardsConstantInterface.NiuSeven);
		
		onSpecifiedCardTypeTestCase(new int[]{3, 6, 4, 8, 1}, BullCardsConstantInterface.NiuNine);
		
		onSpecifiedCardTypeTestCase(new int[]{3, 6, 8, 1, 5}, BullCardsConstantInterface.NiuSix);
		
		onSpecifiedCardTypeTestCase(new int[]{3, 6, 8, 1, 6}, BullCardsConstantInterface.NiuSeven);
		
		onSpecifiedCardTypeTestCase(new int[]{3, 6, 8, 1, 7}, BullCardsConstantInterface.NiuEight);
		
		onSpecifiedCardTypeTestCase(new int[]{3, 6, 1, 8, 8}, BullCardsConstantInterface.NiuPairOfKing_8);
		
		onSpecifiedCardTypeTestCase(new int[]{3, 6, 8, 1, 9}, BullCardsConstantInterface.NiuNiu);
		
		onSpecifiedCardTypeTestCase(new int[]{3, 6, 1, 8, 10}, BullCardsConstantInterface.NiuEight);
		
		onSpecifiedCardTypeTestCase(new int[]{3, 6, 1, 8, 11}, BullCardsConstantInterface.NiuEight);
		
		onSpecifiedCardTypeTestCase(new int[]{3, 6, 1, 8, 12}, BullCardsConstantInterface.NiuEight);
		
		onSpecifiedCardTypeTestCase(new int[]{3, 6, 1, 8, 13}, BullCardsConstantInterface.NiuEight);
		
		onSpecifiedCardTypeTestCase(new int[]{3, 6, 4, 1, 1}, BullCardsConstantInterface.NiuPairOfAces);
		
		onSpecifiedCardTypeTestCase(new int[]{3, 6, 1, 4, 2}, BullCardsConstantInterface.NiuSix);
		
		onBestCardTypeTestCase(new int[]{3, 1, 3, 6, 4}, BullCardsConstantInterface.NiuNiu);
		
		onBestCardTypeTestCase(new int[]{3, 6, 1, 4, 4}, BullCardsConstantInterface.NiuPairOfKing_4);
		
		onBestCardTypeTestCase(new int[]{3, 6, 1, 4, 5}, BullCardsConstantInterface.NiuNine);
		
		onBestCardTypeTestCase(new int[]{3, 6, 1, 4, 6}, BullCardsConstantInterface.NiuNiu);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 1, 7}, BullCardsConstantInterface.NiuEight);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 1, 8}, BullCardsConstantInterface.NiuNine);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 1, 9}, BullCardsConstantInterface.NiuNiu);
		
		onBestCardTypeTestCase(new int[]{6, 4, 10, 3, 1}, BullCardsConstantInterface.NiuSeven);
		
		onBestCardTypeTestCase(new int[]{6, 4, 11, 3, 1}, BullCardsConstantInterface.NiuSeven);
		
		onBestCardTypeTestCase(new int[]{6, 4, 12, 3, 1}, BullCardsConstantInterface.NiuSeven);
		
		onBestCardTypeTestCase(new int[]{6, 4, 13, 3, 1}, BullCardsConstantInterface.NiuSeven);
		
		onBestCardTypeTestCase(new int[]{1, 3, 6, 3, 3}, BullCardsConstantInterface.NiuPairOfKing_3);
		
		onBestCardTypeTestCase(new int[]{3, 3, 1, 3, 3}, BullCardsConstantInterface.NiuPairOfKing_3);
		
		onBestCardTypeTestCase(new int[]{3, 3, 1, 6, 6}, BullCardsConstantInterface.NiuPairOfKing_6);
		
		onBestCardTypeTestCase(new int[]{4, 3, 6, 3, 3}, BullCardsConstantInterface.NiuPairOfKing_3);
		
		onBestCardTypeTestCase(new int[]{3, 3, 4, 3, 3}, BullCardsConstantInterface.NiuPairOfKing_3);
		
		onBestCardTypeTestCase(new int[]{3, 3, 4, 6, 6}, BullCardsConstantInterface.NiuPairOfKing_6);
		
		onBestCardTypeTestCase(new int[]{8, 3, 6, 3, 3}, BullCardsConstantInterface.NiuPairOfKing_3);
		
		onBestCardTypeTestCase(new int[]{3, 3, 8, 3, 3}, BullCardsConstantInterface.NiuPairOfKing_3);
		
		onBestCardTypeTestCase(new int[]{3, 3, 8, 6, 6}, BullCardsConstantInterface.NiuPairOfKing_6);
		
		onBestCardTypeTestCase(new int[]{3, 1, 3, 6, 6}, BullCardsConstantInterface.NiuPairOfKing_6);
		
		onBestCardTypeTestCase(new int[]{3, 6, 1, 3, 3}, BullCardsConstantInterface.NiuPairOfKing_3);
		
		onBestCardTypeTestCase(new int[]{3, 6, 1, 6, 6}, BullCardsConstantInterface.NiuPairOfKing_6);
		
		onBestCardTypeTestCase(new int[]{3, 4, 3, 6, 6}, BullCardsConstantInterface.NiuPairOfKing_6);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 3, 3}, BullCardsConstantInterface.NiuPairOfKing_3);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 6, 6}, BullCardsConstantInterface.NiuPairOfKing_6); 
		
		onBestCardTypeTestCase(new int[]{3, 8, 3, 6, 6}, BullCardsConstantInterface.NiuPairOfKing_6); 
		
		onBestCardTypeTestCase(new int[]{3, 6, 8, 3, 3}, BullCardsConstantInterface.NiuPairOfKing_3); 	
		
		onBestCardTypeTestCase(new int[]{3, 6, 8, 6, 6}, BullCardsConstantInterface.NiuPairOfKing_6);

		/////////////////////////////////////////////////////////////////////////////////////////////
		onSpecifiedCardTypeTestCase(new int[]{39, 29, 6, 53, 36}, BullCardsConstantInterface.NiuNine);

		onSpecifiedCardTypeTestCase(new int[]{54, 37, 2, 39, 55}, BullCardsConstantInterface.NiuPairOfKing_7);
		
		onSpecifiedCardTypeTestCase(new int[]{2, 3, 5, 4, 6}, BullCardsConstantInterface.NiuNiu);
		
		onSpecifiedCardTypeTestCase(new int[]{7, 2, 33, 12, 49}, BullCardsConstantInterface.NiuBullOfSpades);
		
		System.out.println("onBullCardsSpecificCardTypeTestCase pass.");
	}
	
	private void onBestCardTypeTestCase(int[] handCard, int cardType){
		List<Integer> cards = new ArrayList<Integer>();
		for (int i = 0; i < handCard.length; i++) {
			cards.add(handCard[i]);
		}
		
		int bestCardType = 0;
		for(Integer type : logic.getBestCard(cards).keySet()){
			bestCardType = type;
		}
		assert bestCardType == cardType;
		/*
		int curType = logic.onGetHandCardsInfo(cards);
		System.out.println("=========== curType = " + curType);
		assert curType == cardType;
		*/
	}
	
	public void onBullCardsBestCardTypeTestCase(){
		
		System.out.println("onBullCardsBestCardTypeTestCase start.");

		onBestCardTypeTestCase(new int[]{3, 6, 8, 1, 1}, BullCardsConstantInterface.NiuPairOfAces);

		onBestCardTypeTestCase(new int[]{3, 6, 8, 1, 2}, BullCardsConstantInterface.NiuNiu);

		onBestCardTypeTestCase(new int[]{3, 6, 8, 1, 3}, BullCardsConstantInterface.NiuSeven);
		
		onBestCardTypeTestCase(new int[]{3, 6, 8, 1, 4}, BullCardsConstantInterface.NiuNine);
		
		onBestCardTypeTestCase(new int[]{3, 6, 8, 1, 5}, BullCardsConstantInterface.NiuSix);
		
		onBestCardTypeTestCase(new int[]{3, 6, 8, 1, 6}, BullCardsConstantInterface.NiuSeven);
		
		onBestCardTypeTestCase(new int[]{3, 6, 8, 1, 7}, BullCardsConstantInterface.NiuEight);
		
		onBestCardTypeTestCase(new int[]{3, 6, 8, 1, 8}, BullCardsConstantInterface.NiuPairOfKing_8);
		
		onBestCardTypeTestCase(new int[]{3, 6, 8, 1, 9}, BullCardsConstantInterface.NiuNiu);
		
		onBestCardTypeTestCase(new int[]{3, 6, 8, 1, 10}, BullCardsConstantInterface.NiuEight);
		
		onBestCardTypeTestCase(new int[]{3, 6, 8, 1, 11}, BullCardsConstantInterface.NiuEight);
		
		onBestCardTypeTestCase(new int[]{3, 6, 8, 1, 12}, BullCardsConstantInterface.NiuEight);
		
		onBestCardTypeTestCase(new int[]{3, 6, 8, 1, 13}, BullCardsConstantInterface.NiuEight);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 1, 1}, BullCardsConstantInterface.NiuPairOfAces);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 1, 2}, BullCardsConstantInterface.NiuSix);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 1, 3}, BullCardsConstantInterface.NiuNiu);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 1, 4}, BullCardsConstantInterface.NiuPairOfKing_4);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 1, 5}, BullCardsConstantInterface.NiuNine);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 1, 6}, BullCardsConstantInterface.NiuNiu);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 1, 7}, BullCardsConstantInterface.NiuEight);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 1, 8}, BullCardsConstantInterface.NiuNine);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 1, 9}, BullCardsConstantInterface.NiuNiu);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 1, 10}, BullCardsConstantInterface.NiuSeven);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 1, 11}, BullCardsConstantInterface.NiuSeven);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 1, 12}, BullCardsConstantInterface.NiuSeven);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 1, 13}, BullCardsConstantInterface.NiuSeven);
		
		onBestCardTypeTestCase(new int[]{3, 3, 1, 3, 6}, BullCardsConstantInterface.NiuPairOfKing_3);
		
		onBestCardTypeTestCase(new int[]{3, 3, 1, 3, 3}, BullCardsConstantInterface.NiuPairOfKing_3);
		
		onBestCardTypeTestCase(new int[]{3, 3, 1, 6, 6}, BullCardsConstantInterface.NiuPairOfKing_6);
		
		onBestCardTypeTestCase(new int[]{3, 3, 4, 3, 6}, BullCardsConstantInterface.NiuPairOfKing_3);
		
		onBestCardTypeTestCase(new int[]{3, 3, 4, 3, 3}, BullCardsConstantInterface.NiuPairOfKing_3);
		
		onBestCardTypeTestCase(new int[]{3, 3, 4, 6, 6}, BullCardsConstantInterface.NiuPairOfKing_6);
		
		onBestCardTypeTestCase(new int[]{3, 3, 8, 3, 6}, BullCardsConstantInterface.NiuPairOfKing_3);
		
		onBestCardTypeTestCase(new int[]{3, 3, 8, 3, 3}, BullCardsConstantInterface.NiuPairOfKing_3);
		
		onBestCardTypeTestCase(new int[]{3, 3, 8, 6, 6}, BullCardsConstantInterface.NiuPairOfKing_6);
		
		onBestCardTypeTestCase(new int[]{3, 6, 1, 3, 6}, BullCardsConstantInterface.NiuPairOfKing_6);
		
		onBestCardTypeTestCase(new int[]{3, 6, 1, 3, 3}, BullCardsConstantInterface.NiuPairOfKing_3);
		
		onBestCardTypeTestCase(new int[]{3, 6, 1, 6, 6}, BullCardsConstantInterface.NiuPairOfKing_6);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 3, 6}, BullCardsConstantInterface.NiuPairOfKing_6);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 3, 3}, BullCardsConstantInterface.NiuPairOfKing_3);
		
		onBestCardTypeTestCase(new int[]{3, 6, 4, 6, 6}, BullCardsConstantInterface.NiuPairOfKing_6); 
		
		onBestCardTypeTestCase(new int[]{3, 6, 8, 3, 6}, BullCardsConstantInterface.NiuPairOfKing_6); 
		
		onBestCardTypeTestCase(new int[]{3, 6, 8, 3, 3}, BullCardsConstantInterface.NiuPairOfKing_3); 	
		
		onBestCardTypeTestCase(new int[]{3, 6, 8, 6, 6}, BullCardsConstantInterface.NiuPairOfKing_6);
		
		///////////////////////////////////////////////////////////////////////////////
		
		onBestCardTypeTestCase(new int[]{39, 53, 29, 36, 6}, BullCardsConstantInterface.NiuNine);
		
		onBestCardTypeTestCase(new int[]{39, 54, 55, 37, 2}, BullCardsConstantInterface.NiuPairOfKing_7);
		
		onBestCardTypeTestCase(new int[]{2, 3, 4, 5, 6}, BullCardsConstantInterface.NiuNiu);
		
		onBestCardTypeTestCase(new int[]{7, 49, 33, 12, 2}, BullCardsConstantInterface.NiuBullOfSpades);
		
		System.out.println("onBullCardsBestCardTypeTestCase pass.");
		
	}
}
