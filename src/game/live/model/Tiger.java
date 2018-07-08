package game.live.model;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.live.util.ConvertUtil;
import netty.util.WeightRandom;
import protocols.live.bettiger.line;
import protocols.live.bettiger.reward;
import protocols.live.common.scroll;
import protocols.live.common.tiger;

public class Tiger {
	private static Logger logger = LoggerFactory.getLogger(Tiger.class);
	public static class P {
		public int x;
		public int y;
		public P(int x, int y){
			this.x = x;
			this.y = y;
		}
		@Override
		public int hashCode() {
			return x * 10 + y;
		}
		@Override
		public boolean equals(Object anObject) {
			if (this == anObject) {
	            return true;
	        }
	        if (anObject instanceof P) {
	        	P other = (P)anObject;
	            if(this.x == other.x && this.y == other.y){
	            	return true;
	            }
	        }
	        return false;
		}
	}
	
	static class Line {
		int id;
		List<P> ps = new LinkedList<>();
		public Line(int id, P p1, P p2, P p3, P p4, P p5){
			this.id = id;
			ps.add(p1);
			ps.add(p2);
			ps.add(p3);
			ps.add(p4);
			ps.add(p5);
		}
	}
	
	public static final int COLUMN = 5;
	public static final int ROW = 10;
	public static final int REWARD_ROW = 3;
	
	private static List<Line> lines = new LinkedList<>();
	// Map<pivot, Map<combo, multiple>
	private static Map<String, Map<Integer, Integer>> muls = new HashMap<>();
	private static WeightRandom<String> random = new WeightRandom<>();
	private static SecureRandom posRan = new SecureRandom();
	static {
		Map<String, Integer> poss = new HashMap<>();
		poss.put("1", 10);   //财神爷
		poss.put("2", 20);  //舞狮头
		poss.put("3", 30);  //鞭炮
		poss.put("4", 40);  //红包
		poss.put("5", 50);  //银元宝
		poss.put("6", 80);  //A
		poss.put("7", 90);  //K
		poss.put("8", 100);  //Q
		poss.put("9", 130);  //J
		poss.put("10", 150); //10
		poss.put("11", 25); //Wild
		poss.put("12", 0); //Bonus
		poss.put("13", 0); //财神爷Wild
		random.setWeightedObjects(poss);
		lines.add(new Line(1, new P(0,1), new P(1,1), new P(2,1), new P(3,1), new P(4,1)));
		lines.add(new Line(2, new P(0,0), new P(1,0), new P(2,0), new P(3,0), new P(4,0)));
		lines.add(new Line(3, new P(0,2), new P(1,2), new P(2,2), new P(3,2), new P(4,2)));
		lines.add(new Line(4, new P(0,0), new P(1,1), new P(2,2), new P(3,1), new P(4,0)));
		lines.add(new Line(5, new P(0,2), new P(1,1), new P(2,0), new P(3,1), new P(4,2)));
		lines.add(new Line(6, new P(0,0), new P(1,0), new P(2,1), new P(3,2), new P(4,2)));
		lines.add(new Line(7, new P(0,2), new P(1,2), new P(2,1), new P(3,0), new P(4,0)));
		lines.add(new Line(8, new P(0,1), new P(1,2), new P(2,0), new P(3,2), new P(4,1)));
		lines.add(new Line(9, new P(0,1), new P(1,0), new P(2,2), new P(3,0), new P(4,1)));
		lines.add(new Line(10, new P(0,0), new P(1,1), new P(2,0), new P(3,1), new P(4,0)));
		lines.add(new Line(11, new P(0,2), new P(1,1), new P(2,2), new P(3,1), new P(4,2)));
		lines.add(new Line(12, new P(0,1), new P(1,0), new P(2,1), new P(3,2), new P(4,1)));
		lines.add(new Line(13, new P(0,1), new P(1,2), new P(2,1), new P(3,0), new P(4,1)));
		lines.add(new Line(14, new P(0,0), new P(1,1), new P(2,1), new P(3,1), new P(4,0)));
		lines.add(new Line(15, new P(0,2), new P(1,1), new P(2,1), new P(3,1), new P(4,2)));
		lines.add(new Line(16, new P(0,1), new P(1,0), new P(2,0), new P(3,0), new P(4,1)));
		lines.add(new Line(17, new P(0,1), new P(1,2), new P(2,2), new P(3,2), new P(4,1)));
		lines.add(new Line(18, new P(0,0), new P(1,2), new P(2,2), new P(3,2), new P(4,0)));
		lines.add(new Line(19, new P(0,2), new P(1,0), new P(2,0), new P(3,0), new P(4,2)));
		lines.add(new Line(20, new P(0,0), new P(1,0), new P(2,2), new P(3,0), new P(4,0)));
		lines.add(new Line(21, new P(0,2), new P(1,2), new P(2,0), new P(3,2), new P(4,2)));
		lines.add(new Line(22, new P(0,1), new P(1,1), new P(2,0), new P(3,1), new P(4,1)));
		lines.add(new Line(23, new P(0,1), new P(1,1), new P(2,2), new P(3,1), new P(4,1)));
		lines.add(new Line(24, new P(0,0), new P(1,2), new P(2,0), new P(3,2), new P(4,0)));
		lines.add(new Line(25, new P(0,2), new P(1,0), new P(2,2), new P(3,0), new P(4,2)));
		lines.add(new Line(26, new P(0,2), new P(1,0), new P(2,1), new P(3,2), new P(4,0)));
		lines.add(new Line(27, new P(0,0), new P(1,2), new P(2,1), new P(3,0), new P(4,2)));
		lines.add(new Line(28, new P(0,0), new P(1,2), new P(2,1), new P(3,2), new P(4,0)));
		lines.add(new Line(29, new P(0,2), new P(1,0), new P(2,1), new P(3,0), new P(4,2)));
		lines.add(new Line(30, new P(0,2), new P(1,1), new P(2,0), new P(3,0), new P(4,1)));
		lines.add(new Line(31, new P(0,0), new P(1,1), new P(2,2), new P(3,2), new P(4,1)));
		lines.add(new Line(32, new P(0,0), new P(1,1), new P(2,0), new P(3,1), new P(4,2)));
		lines.add(new Line(33, new P(0,2), new P(1,1), new P(2,2), new P(3,1), new P(4,0)));
		lines.add(new Line(34, new P(0,1), new P(1,0), new P(2,2), new P(3,1), new P(4,2)));
		lines.add(new Line(35, new P(0,1), new P(1,2), new P(2,0), new P(3,1), new P(4,0)));
		lines.add(new Line(36, new P(0,2), new P(1,2), new P(2,0), new P(3,0), new P(4,0)));
		lines.add(new Line(37, new P(0,0), new P(1,0), new P(2,2), new P(3,2), new P(4,2)));
		lines.add(new Line(38, new P(0,0), new P(1,0), new P(2,1), new P(3,1), new P(4,2)));
		lines.add(new Line(39, new P(0,2), new P(1,2), new P(2,1), new P(3,1), new P(4,0)));
		lines.add(new Line(40, new P(0,0), new P(1,1), new P(2,1), new P(3,2), new P(4,2)));
		lines.add(new Line(41, new P(0,2), new P(1,1), new P(2,1), new P(3,0), new P(4,0)));
		lines.add(new Line(42, new P(0,2), new P(1,1), new P(2,0), new P(3,0), new P(4,0)));
		lines.add(new Line(43, new P(0,0), new P(1,1), new P(2,2), new P(3,2), new P(4,2)));
		lines.add(new Line(44, new P(0,1), new P(1,0), new P(2,0), new P(3,1), new P(4,1)));
		lines.add(new Line(45, new P(0,1), new P(1,2), new P(2,2), new P(3,1), new P(4,1)));
		lines.add(new Line(46, new P(0,1), new P(1,1), new P(2,0), new P(3,0), new P(4,1)));
		lines.add(new Line(47, new P(0,1), new P(1,1), new P(2,2), new P(3,2), new P(4,1)));
		lines.add(new Line(48, new P(0,2), new P(1,1), new P(2,0), new P(3,0), new P(4,2)));
		lines.add(new Line(49, new P(0,0), new P(1,1), new P(2,2), new P(3,2), new P(4,0)));
		lines.add(new Line(50, new P(0,0), new P(1,1), new P(2,1), new P(3,1), new P(4,2)));
		Map<Integer, Integer> map1 = new HashMap<>();map1.put(2, 5);map1.put(3, 50);map1.put(4, 350);map1.put(5, 2000);
		Map<Integer, Integer> map2 = new HashMap<>();map2.put(2, 3);map2.put(3, 30);map2.put(4, 250);map2.put(5, 1000);
		Map<Integer, Integer> map3 = new HashMap<>();map3.put(3, 25);map3.put(4, 200);map3.put(5, 700);
		Map<Integer, Integer> map4 = new HashMap<>();map4.put(3, 20);map4.put(4, 150);map4.put(5, 500);
		Map<Integer, Integer> map5 = new HashMap<>();map5.put(3, 15);map5.put(4, 100);map5.put(5, 350);
		Map<Integer, Integer> map6 = new HashMap<>();map6.put(3, 10);map6.put(4, 75);map6.put(5, 300);
		Map<Integer, Integer> map7 = new HashMap<>();map7.put(3, 10);map7.put(4, 75);map7.put(5, 250);
		Map<Integer, Integer> map8 = new HashMap<>();map8.put(3, 5);map8.put(4, 50);map8.put(5, 200);
		Map<Integer, Integer> map9 = new HashMap<>();map9.put(3, 5);map9.put(4, 50);map9.put(5, 150);
		Map<Integer, Integer> map10 = new HashMap<>();map10.put(3, 5);map10.put(4, 50);map10.put(5, 100);
		muls.put("1", map1);
		muls.put("2", map2);
		muls.put("3", map3);
		muls.put("4", map4);
		muls.put("5", map5);
		muls.put("6", map6);
		muls.put("7", map7);
		muls.put("8", map8);
		muls.put("9", map9);
		muls.put("10", map10);
	}
	
	private String[][] item = new String[COLUMN][ROW];
	private int[] pos = new int[COLUMN];
	private String[][] rewardItem = new String[Tiger.COLUMN][Tiger.REWARD_ROW];
	private int lineCount;
	private int bet;
	
	// Reward相关
	private Set<P> allPoints = new HashSet<>();
	private reward.Builder detail = reward.newBuilder();
	private int totalReward = 0;
	
	public Tiger(){
		scroll();
	}
	
	public void scroll(){
		for(int i=0;i<COLUMN;i++){
			for(int j=0;j<ROW;j++){
				item[i][j] = random.next();
			}
			pos[i] = posRan.nextInt(ROW-2);//0-7
		}
	}
	
	public Tiger bet(int lineCount, int bet){
		if(lineCount <= 0){
			lineCount = 50;
		}
		this.lineCount = lineCount;
		this.bet = bet;
		return this;
	}
	
	public void openPrize(){
		scroll();
		allPoints = new HashSet<>();
		detail = reward.newBuilder();
		totalReward = 0;
		rewardItem = new String[5][3];
		for(int i=0;i<COLUMN;i++){
			for(int j=0;j<REWARD_ROW;j++){
				rewardItem[i][j] = item[i][pos[i]+j];
			}
		}
		calReward();
	}
	
	private void calReward(){
		for(int i=0;i<lineCount;i++){
			Line l = lines.get(i);
			String pivot = getPivot(l);
			int combo = 1;
			for(int j=1;j<Tiger.COLUMN;j++){
				P p = l.ps.get(j);
				String item = rewardItem[p.x][p.y];
				if(!item.equals(pivot) && !isWild(item)){
					break;
				}
				combo++;
			}
			if(combo == 1 || isWild(pivot)){
				continue;
			}
			if(combo == 2 && !pivot.equals("1") && !pivot.equals("2")){
				continue;
			}
			Map<Integer, Integer> map = muls.get(pivot);
			if(map == null){
				continue;
			}
			for(int c=2;c<=combo;c++){
				if(c == 2 && !pivot.equals("1") && !pivot.equals("2")){
					continue;
				}
				line.Builder bld = line.newBuilder();
				bld.setId(l.id);
				bld.setCombo(c);
				for(int j=0;j<c;j++){
					P oriP = l.ps.get(j);
					P p = new P(oriP.x, oriP.y+pos[j]);
					bld.addPoints(ConvertUtil.toPoint(p));
					if(c == combo){
						allPoints.add(p);
					}
				}
				bld.setItem(pivot);
				Integer mul = map.get(c);
				if(mul == null){
					logger.error("pivot({}), combo({}), bet({})", pivot, c, bet);
					continue;
				}
				int reward = mul * bet;
				bld.setBet(bet);
				bld.setMul(mul);
				bld.setReward(reward);
				detail.addLines(bld);
				totalReward += reward;
			}
		}
		for(P p : allPoints){
			detail.addAllPoints(ConvertUtil.toPoint(p));
		}
	}
	
	private String getPivot(Line line){
		for(int i=0;i<Tiger.COLUMN;i++){
			P p = line.ps.get(i);
			String item = rewardItem[p.x][p.y];
			if(!isWild(item)){
				return item;
			}
		}
		return "11";
	}
	
	public boolean isWild(String item){
		return "11".equals(item);
	}

	public reward.Builder getDetail() {
		return detail;
	}

	public int getTotalReward() {
		return totalReward;
	}
	
	public tiger toClient(){
		tiger.Builder builder = tiger.newBuilder();
		for(int i=0;i<COLUMN;i++){
			scroll.Builder wheel = scroll.newBuilder();
			for(int j=0;j<ROW;j++){
				wheel.addItem(item[i][j]);
			}
			wheel.setPos(pos[i]);
			builder.addWheel(wheel);
		}
		return builder.build();
	}
}
