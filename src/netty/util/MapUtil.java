package netty.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {

	public static Map<String, Integer> sumMap(Map<String, Integer> sum1, Map<String, Integer> sum2){
    	Map<String, Integer> sum = new HashMap<>();
    	for(Map.Entry<String, Integer> entry : sum1.entrySet()){
    		String target = entry.getKey();
    		int coin1 = entry.getValue();
    		Integer coin2 = sum2.get(target);
    		if(coin2 == null){
    			coin2 = 0;
    		}
    		sum.put(target, coin1 + coin2);
    	}
    	return sum;
    }
}
