package game.live.robot;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mozat.morange.dbcache.tables.LiveName;

import netty.util.StringUtil;
import netty.util.WeightRandom;

public class LiveNameMgr {
	private static Map<String, Integer> names = new ConcurrentHashMap<String, Integer>();
    private static WeightRandom<String> nameRandom;

    public static void init() {
    	names.clear();
    	List<LiveName> all = LiveName.getAllObjects();
		for(LiveName one: all){
			names.put(one.NAME, one.WEIGHT);
		}
		nameRandom = new WeightRandom<String>(names);
    }

    public static String randomName(){
	    return StringUtil.nonNull(nameRandom.next());
    }
}
