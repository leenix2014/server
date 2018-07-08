package netty.GameEvaluation;



public class BullCardsConstantInterface {
	
	public static final int BullGame = 1;		// 斗牛
	
	public static final int ThreeCardGame = 2;	// 三五张
	
	public enum SUITS {
		DIAMONDS, 	// 方块
		CLUBS, 		// 梅花
		HEARTS, 	// 红桃
		SPADES,		// 黑桃
	};

	public static final int NoNiu = 0x0100;//256
	
	public static final int NiuOne = 0x0101;//257
	
	public static final int NiuTwo = 0x0102;//258
	
	public static final int NiuThree = 0x0103;//259
	
	public static final int NiuFour = 0x0104;//260
	
	public static final int NiuFive = 0x0105;//261
	
	public static final int NiuSix = 0x0106;//262
	
	public static final int NiuSeven = 0x0107;//263
	
	public static final int NiuEight = 0x0108;//264
	
	public static final int NiuNine = 0x0109;//265
	
	public static final int NiuNiu =  0x0201;			// 牛牛，两倍  513

	public static final int NiuPairOfKing_2 = 0x0302;	// 牛+对子2 770
	
	public static final int NiuPairOfKing_3 = 0x0303;	// 牛+对子3 771
	
	public static final int NiuPairOfKing_4 = 0x0304;	// 牛+对子4 772
	
	public static final int NiuPairOfKing_5 = 0x0305;	// 牛+对子5 773
	
	public static final int NiuPairOfKing_6 = 0x0306;	// 牛+对子6 774
	
	public static final int NiuPairOfKing_7 = 0x0307;	// 牛+对子7 775
	
	public static final int NiuPairOfKing_8 = 0x0308;	// 牛+对子8 776
	
	public static final int NiuPairOfKing_9 = 0x0309;	// 牛+对子9 777
	
	public static final int NiuPairOfKing_10 = 0x030A;	// 牛+对子10 778
	
	public static final int NiuPairOfKing_11 = 0x030B;	// 牛+对子J 779
	
	public static final int NiuPairOfKing_12 = 0x030C;	// 牛+对子Q 780
	
	public static final int NiuPairOfKing_13 = 0x030D;  // 牛+对子K 781
	
	public static final int NiuPairOfAces = 0x0401;		// 牛双A，三倍 1025
	
	public static final int NiuBullOfSpades = 0x0501;	// 牛冬菇=牛+人头像（J、Q、K）+黑桃A，四倍  1281
	
	public static final int NiuFiveDukes = 0x0801;		// 牛五爵=五张人头像(J、Q、K)，五倍 2049
	
	// 三张结算牌型
	public static final int ThreeDukes = 11;	// 三爵士=三张人头像(J、Q、K)，三倍
	
	public static final int ThreeSame = 12;		// 三条=三张同点数的牌，四倍
	
	public static final int ThreeAce = 13;		// 三ACE=三张A，五倍

}
