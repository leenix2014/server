package netty.util;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class WeightRandom<T> {
	
	private Map<T, Integer> weightedObjects;
	
	private int totalWeight;
	
	private SecureRandom random = new SecureRandom();
	
	public WeightRandom(){
		
	}
	
	public WeightRandom(Map<T, Integer> weightedObjects){
		this.setWeightedObjects(weightedObjects);
	}
	
	public void setWeightedObjects(Map<T, Integer> weightedObjects){
		this.weightedObjects = weightedObjects;
		totalWeight = 0;
		for(int weight : weightedObjects.values()){
			totalWeight += weight;
		}
	}
	
	public T next(){
		if(totalWeight <= 0){
			return null;
		}
		int index = random.nextInt(totalWeight);
		return getByIndex(index);
	}
	
	private T getByIndex(int index){
		int total = 0;
		for(Map.Entry<T, Integer> entry : weightedObjects.entrySet()){
			T target = entry.getKey();
			Integer segment = entry.getValue();
			total += segment;
			if(index < total){
				return target;
			}
		}
		return null;//should not reach here
	}

	public static void main(String[] args) {
		Map<String, Integer> testMap = new HashMap<String, Integer>();
		testMap.put("1", 20);
		testMap.put("2", 20);
		testMap.put("3", 20);
		testMap.put("4", 20);
		testMap.put("5", 20);
		
		Map<String, Integer> statMap = new HashMap<String, Integer>();
		
		WeightRandom<String> ran = new WeightRandom<String>(testMap);
		for(int i=0;i<1000000;i++){
			String result = ran.next();
			Integer sum = statMap.get(result);
			if(sum == null){
				statMap.put(result, 1);
			} else {
				statMap.put(result, sum+1);
			}
		}
		
		for(Map.Entry<String, Integer> entry : statMap.entrySet()){
			System.out.println(entry.getKey() + ":"+entry.getValue());
		}
	}

}
