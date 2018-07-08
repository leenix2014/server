package game.common;

public class Poker {
	public static enum SUITS {
		DIAMONDS, 	// 方块
		CLUBS, 		// 梅花
		HEARTS, 	// 红桃
		SPADES,		// 黑桃
	};
	
	private int round;      // 花色，0=方块 1=梅花 2=红桃 3=黑桃, for history reason
	private SUITS color;	// 花色，enum
	private int point;		// 牌面点数
	private int weight;		// 10、J、Q、K=10, others=牌面数字
	
	public static Poker getPoker(int card){
		int round = (card & 0xF0) >> 4;
		int value = card & 0x0F;
		Poker poker = new Poker();
		poker.round = round;
		if(3 == round){
			poker.color = SUITS.SPADES;
		} else if(2 == round){
			poker.color = SUITS.HEARTS;
		} else if(1 == round){
			poker.color = SUITS.CLUBS;
		} else {
			poker.color = SUITS.DIAMONDS;
		}
		poker.point = value;
		poker.weight = value > 10 ? 10 : value;
		return poker;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public SUITS getColor() {
		return color;
	}

	public void setColor(SUITS color) {
		this.color = color;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int value) {
		this.point = value;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
}
