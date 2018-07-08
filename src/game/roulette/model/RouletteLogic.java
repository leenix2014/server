package game.roulette.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.common.CommonConfig;
import netty.util.MathUtil;
import protocols.roulette.common.beton;

public class RouletteLogic {
	
	protected static Logger logger = LoggerFactory.getLogger(RouletteLogic.class);
	private static final Map<String, MySet> hitNums = new LinkedHashMap<String, MySet>();
	private static final Set<String> blinkArea = new HashSet<String>();
	
	private static class MySet {
		private Set<Integer> nums = new LinkedHashSet<Integer>();
		public MySet add(int num){
			nums.add(num);
			return this;
		}
		
		public MySet addRange(int min, int max){
			for(int i=min;i<=max;i++){
				nums.add(i);
			}
			return this;
		}
		
		public MySet addEven(int min, int max){
			return addMod(min, max, 2, 0);
		}
		
		public MySet addOdd(int min, int max){
			return addMod(min, max, 2, 1);
		}
		
		public MySet addMod(int min, int max, int divisor, int remainder){
			for(int i=min;i<=max;i++){
				if(i % divisor == remainder){
					nums.add(i);
				}
			}
			return this;
		}
		
		public boolean isHit(int result){
			return nums.contains(result);
		}
		
		public float getPeiLv(){
			int roulettePercent = CommonConfig.getPercent(CommonConfig.ROULETTE_PERCENT, 5);
			float peilv = 36/nums.size();
			peilv = peilv * (100-roulettePercent)/100;
			return peilv;
		}

		@Override
		public String toString() {
			return nums.toString();
		}
	}
	
	static {
		hitNums.put("small", new MySet().addRange(1, 18));blinkArea.add("small");
		hitNums.put("big", new MySet().addRange(19, 36));blinkArea.add("big");
		hitNums.put("even", new MySet().addEven(1, 36));blinkArea.add("even");
		hitNums.put("odd", new MySet().addOdd(1, 36));blinkArea.add("odd");
		hitNums.put("red", new MySet().addMod(1, 9, 2, 1).addMod(12, 18, 2, 0)
				.addMod(19, 27, 2, 1).addMod(30, 36, 2, 0));blinkArea.add("red");
		hitNums.put("black", new MySet().addMod(2, 10, 2, 0).addMod(11, 17, 2, 1)
				.addMod(20, 28, 2, 0).addMod(29, 35, 2, 1));blinkArea.add("black");
		hitNums.put("dozen1", new MySet().addRange(1, 12));blinkArea.add("dozen1");
		hitNums.put("dozen2", new MySet().addRange(13, 24));blinkArea.add("dozen2");
		hitNums.put("dozen3", new MySet().addRange(25, 36));blinkArea.add("dozen3");
		hitNums.put("col1", new MySet().addMod(1, 36, 3, 1));blinkArea.add("col1");
		hitNums.put("col2", new MySet().addMod(1, 36, 3, 2));blinkArea.add("col2");
		hitNums.put("col3", new MySet().addMod(1, 36, 3, 0));blinkArea.add("col3");
		for(beton target : beton.values()){
			String name = target.name();
			if(name.startsWith("s") && !name.startsWith("sx") && !name.startsWith("small")){
				// Straight Bet;
				String straight = name.substring(1, name.length());
				hitNums.put(name, new MySet().add(MathUtil.parseInt(straight)));
				blinkArea.add(name);
			} else if(name.startsWith("d") && !name.startsWith("dozen")){
				// Split Bet
				String split = name.substring(1, name.length());
				String[] parts = split.split("_");
				MySet set = new MySet();
				for(String part : parts){
					set.add(MathUtil.parseInt(part));
				}
				hitNums.put(name, set);
			} else if(name.startsWith("t")){
				// Street Bet
				String street = name.substring(1, name.length());
				if("0_1_2".equals(street)){
					hitNums.put(name, new MySet().addRange(0, 2));
				} else if("0_2_3".equals(street)){
					hitNums.put(name, new MySet().add(0).add(2).add(3));
				} else {
					int first = MathUtil.parseInt(street);
					hitNums.put(name, new MySet().add(first).add(first+1).add(first+2));
				}
			} else if(name.startsWith("f")){
				// Corner Bet
				String corner = name.substring(1, name.length());
				int first = MathUtil.parseInt(corner);
				hitNums.put(name, new MySet().add(first).add(first+1).add(first+3).add(first+4));
			} else if(name.startsWith("sx")){
				// Sixline Bet
				String six = name.substring(2, name.length());
				String[] parts = six.split("_");
				hitNums.put(name, new MySet().addRange(MathUtil.parseInt(parts[0]),MathUtil.parseInt(parts[1])));
			}
		}
	}
	
	public static Set<String> getWinTargets(int type, int result){
		Set<String> winTargets = new HashSet<String>();
		for(Map.Entry<String, MySet> entry : hitNums.entrySet()){
			String name = entry.getKey();
			if(!blinkArea.contains(name)){
				continue;
			}
			if(entry.getValue().isHit(result)){
				winTargets.add(entry.getKey());
			}
		}
		return winTargets;
	}
    
	public static Map<String, Integer> getBetResult(int type, int result, Map<String, Integer> userBets){
		Map<String, Integer> results = new HashMap<String, Integer>();
		for(Map.Entry<String, Integer> userBet : userBets.entrySet()){
			String target = userBet.getKey();
			int coins = userBet.getValue();
			MySet arbiter = hitNums.get(target);
			if(arbiter == null){
				logger.warn("Unknown bet target:{}", target);
				results.put(target, 0);//无法识别的下注目标按获奖为0算
				continue;
			}
			if(arbiter.isHit(result)){
				results.put(target, Math.round((coins * arbiter.getPeiLv())));
			} else {
				results.put(target, 0);
			}
		}
		
		return results;
	}
	
	public static void main(String[] args) {
		for(Map.Entry<String, MySet> entry:hitNums.entrySet()){
			System.out.println(entry.getKey()+":"+entry.getValue().toString()+", peilv:"+entry.getValue().getPeiLv());
		}
	}
}
