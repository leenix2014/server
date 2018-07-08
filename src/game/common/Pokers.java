package game.common;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

public class Pokers {

	protected int[] deck = {
		0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D,//方块1到K
	    0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D,//梅花1到K
	    0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D,//红桃1到K
	    0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3A, 0x3B, 0x3C, 0x3D,//黑桃1到K
	};
	
	protected int[] ghost = {
		0x40,//小鬼
		0x50 //大鬼
	};
	
	private int dealingIndex = 0;
	
	private int endIndex;
	
	private boolean needShuffle = false;
	
	private Vector<Integer> cards = new Vector<>();
	private int decks = 1;
	private boolean hasGhost = false;
	
	public Pokers(){
		this(1, false);
	}
	
	public Pokers(int decks){
		this(decks, false);
	}
	
	public Pokers(int decks, boolean hasGhost){
		this.decks = decks;
		for(int i=0; i<decks; i++){
			for(int j=0; j<deck.length;j++){
				cards.add(deck[j]);
			}
			if(hasGhost){
				for(int k=0; k<ghost.length;k++){
					cards.add(ghost[k]);
				}
			}
		}
		shuffle();
	}
	
	public void shuffle(){
		dealingIndex = 0;
		Random rand = new SecureRandom();
		for (int i = 0; i < cards.size(); i++) {
			int idx = rand.nextInt(cards.size());
			Collections.swap(cards, i, idx);
		}
		cut();
	}
	
	public void cut(){
		endIndex = (int) (Math.random() * 200 + 208);
	}
	
	public void cut(int num){
		dealingIndex = (dealingIndex + num) % cards.size();
	}
	
	// 发一张牌
	public Integer deal(){
		if((dealingIndex+1) >= endIndex){
			needShuffle = true;
		}
		int card = cards.get(dealingIndex);
		dealingIndex++;
		return card;
	}
	
	// 发多张牌
	public Vector<Integer> deal(int n){
		Vector<Integer> result = new Vector<Integer>();
		for (int i = 0; i < n; i++) {
			result.add(deal());
		}
		return result;
	}
	
	public int getRemains(){
		return endIndex - dealingIndex;
	}

	public int getDecks() {
		return decks;
	}

	public boolean isHasGhost() {
		return hasGhost;
	}

	public boolean isNeedShuffle() {
		return needShuffle;
	}
	
	public void setNeedShuffle(boolean needShuffle){
		this.needShuffle = needShuffle;
	}
}
