package game.loginReward;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mozat.morange.dbcache.tables.TLoginConfig;

public class LoginRewardConfig{
	//<day,LoginRewardConfig>
	//private static final ObjectCacheUtil<Integer, LoginRewardConfig> map = ObjectCacheUtilManager.create();
	private static Map<Integer, LoginRewardConfig> map = new ConcurrentHashMap<Integer, LoginRewardConfig>();
	
	private final TLoginConfig tLoginRewardConfig;
	
	public static void loadAll(){
		map.clear();
		ArrayList<TLoginConfig> tConfs = TLoginConfig.getAllObjects();
		if (tConfs != null) {
			for(TLoginConfig tConf : tConfs){
				LoginRewardConfig conf = new LoginRewardConfig(tConf);
				map.put(tConf.DAY, conf);
				
				if (tConf.DAY > LoginRewardService.maxLoginDay) {
					LoginRewardService.maxLoginDay = tConf.DAY;
				}
			}
		}
	}
	
	public LoginRewardConfig(TLoginConfig tl) {
		this.tLoginRewardConfig = tl;
	}
	
	public int getRoomCard(){
		return tLoginRewardConfig.ROOM_CARD;
	}
	
	public static LoginRewardConfig getLoginRewardConfig(int day){
		return map.get(day);
	}
}
