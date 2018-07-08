package game.live.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.mozat.morange.dbcache.tables.LiveGift;

import game.common.CommonConfig;
import netty.util.WeightRandom;

public class LiveGiftMgr {

	private static Map<Integer, LiveGift> giftMap = new ConcurrentHashMap<>();
	
	private static List<LiveGift> nonSpurtGift = new Vector<LiveGift>();
	
	private static Map<LiveGift, Integer> weightedObjects = new ConcurrentHashMap<>();
	
    public static void init(){
    	giftMap.clear();
    	nonSpurtGift.clear();
    	weightedObjects.clear();
		String latestVer = CommonConfig.get(CommonConfig.CURR_GIFT_VERSION, "Normal");
		List<LiveGift> query = LiveGift.getManyByCriteria(LiveGift.AttrVERSION.eq(latestVer), LiveGift.AttrIS_VALID.eq(true));
		for(LiveGift gift: query){
			giftMap.put(gift.GIFT_ID, gift);
			if(!gift.SPURT){
				nonSpurtGift.add(gift);
				int weight = (int)(10000/(double)gift.COST);
				weightedObjects.put(gift, weight);
			}
		}
    }

    public static Collection<LiveGift> getGifts(){
	    return giftMap.values();
    }
    
    public static LiveGift randomGift(){
    	WeightRandom<LiveGift> rand = new WeightRandom<LiveGift>(weightedObjects);
		return rand.next();
    }
    
    public static LiveGift getGift(int giftId){
    	return giftMap.get(giftId);
    }
    
    public static List<LiveGift> getGifts(List<Integer> giftIds){
    	List<LiveGift> gifts = new ArrayList<LiveGift>();
    	for(Integer giftId : giftIds){
    		LiveGift gift = nonSpurtGift.get(giftId);
    		if(gift != null){
    			gifts.add(gift);
    		}
    	}
    	return gifts;
    }
}
