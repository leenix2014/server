package game.live.robot;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mozat.morange.dbcache.tables.LiveWord;

import netty.util.WeightRandom;

public class LiveWordMgr {
	private static Map<String, Integer> words = new ConcurrentHashMap<String, Integer>();
    private static WeightRandom<String> wordRandom;

    public static void init() {
    	words.clear();
    	List<LiveWord> all = LiveWord.getAllObjects();
		for(LiveWord one: all){
			words.put(one.WORDS, one.WEIGHT);
		}
		wordRandom = new WeightRandom<String>(words);
    }

    public static String saySomething(){
	    return wordRandom.next();
    }
}
