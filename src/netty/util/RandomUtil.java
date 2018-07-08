package netty.util;

import java.util.HashMap;
import java.util.Map;

public class RandomUtil {

	public static boolean randomBoolean(int truePoss, int falsePoss){
		Map<Boolean, Integer> map = new HashMap<Boolean, Integer>();
		map.put(false, falsePoss);
		map.put(true, truePoss);
		WeightRandom<Boolean> wr = new WeightRandom<Boolean>(map);
		return wr.next();
	}
}
