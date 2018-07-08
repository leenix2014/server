package game.lottery;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.mozat.morange.dbcache.tables.PrizeConfig;
import com.mozat.morange.dbcache.tables.PrizeRecord;
import com.mozat.morange.dbcache.tables.RoomCardBill;
import com.mozat.morange.dbcache.tables.UserDrawCount;
import com.mozat.morange.util.DateUtil;

import netty.GameModels.UserMgr;

public class PrizeService {
	// Map<prizeId, PrizeConfig>
	private static Map<Integer, PrizeConfig> prizeMap = new ConcurrentHashMap<Integer, PrizeConfig>();

	// Map<uid, BingoPrize>
	private static Map<Integer, PrizeConfig> bingoMap = new ConcurrentHashMap<Integer, PrizeConfig>();
	
	public static final int initDrawCount = 3;
	
	private static final int PRIZE_ROOM_CARD = 1;
	
	private static final int PRIZE_DRAW_COUNT = 2;
	
	public static void init() {
		prizeMap.clear();
		List<PrizeConfig> prizes = PrizeConfig.getAllObjects();
		for(PrizeConfig prize : prizes){
			prizeMap.put(prize.PRIZE_ID, prize);
		}
	}
	public static Collection<PrizeConfig> getPrizes() {
		return prizeMap.values();
	}
	
	public static int getUserDrawCount(int userId){
		return getUserDrawRecord(userId).DRAW_COUNT;
	}
	
	private static UserDrawCount getUserDrawRecord(int userId){
		String today = DateUtil.yyyyMMdd(new Date());
		UserDrawCount userDrawCount = UserDrawCount.getOneByCriteria(UserDrawCount.AttrUSER_ID.eq(userId+""),
				UserDrawCount.AttrDRAW_DATE.eq(today));
		if(userDrawCount == null){
			UserDrawCount.removeByCriteria(UserDrawCount.AttrUSER_ID.eq(userId+""),
					UserDrawCount.AttrDRAW_DATE.ne(today));
			userDrawCount = UserDrawCount.create(UserDrawCount.AttrUSER_ID.set(userId+""),
					UserDrawCount.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(userId)),
					UserDrawCount.AttrDRAW_DATE.set(today),
					UserDrawCount.AttrDRAW_COUNT.set(initDrawCount));
		}
		return userDrawCount;
	}
	
	private static void addUserDrawCount(int userId, int count){
		UserDrawCount userDrawCount = getUserDrawRecord(userId);
		userDrawCount.DRAW_COUNT = userDrawCount.DRAW_COUNT + count;
		userDrawCount.update();
	}
	
	public static boolean decreseDrawCount(int userId){
		UserDrawCount userDrawCount = getUserDrawRecord(userId);
		if(userDrawCount.DRAW_COUNT <= 0){
			return false;
		}
		userDrawCount.DRAW_COUNT = userDrawCount.DRAW_COUNT - 1;
		userDrawCount.update();
		return true;
	}
	
	public static PrizeConfig drawPrize(int userId){
		return drawPrize(userId, false);
	}
	
	public static PrizeConfig drawPrize(int userId, boolean firstTime){
		PrizeConfig bingoPrize;
		if(firstTime){
			bingoPrize = drawPrize();
			bingoMap.put(userId, bingoPrize);
		} else {
			bingoPrize = bingoMap.get(userId);
			if(bingoPrize.TYPE == PRIZE_ROOM_CARD){
				int roomCard = bingoPrize.COUNT;
				int oldRoomCardCount = UserMgr.getInstance().getCuber(userId);
				int nowRoomCardCount = oldRoomCardCount + roomCard;
				UserMgr.getInstance().setCuber(userId, nowRoomCardCount);
				RoomCardBill.create(RoomCardBill.AttrUSER_ID.set(userId+""), 
						RoomCardBill.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(userId)),
						RoomCardBill.AttrSOURCE.set("prize"),
						RoomCardBill.AttrSOURCE_ID.set(bingoPrize.PRIZE_ID+""),
						RoomCardBill.AttrSOURCE_NAME.set(bingoPrize.DESC_ZH),
						RoomCardBill.AttrAMOUNT.set(roomCard),
						RoomCardBill.AttrBEFORE_BAL.set(oldRoomCardCount),
						RoomCardBill.AttrAFTER_BAL.set(nowRoomCardCount),
						RoomCardBill.AttrCREATE_TIME.set(new Date()));
			}
			if(bingoPrize.TYPE == PRIZE_DRAW_COUNT){
				addUserDrawCount(userId, bingoPrize.COUNT);
			}
			PrizeRecord.create(PrizeRecord.AttrDRAW_COUNT.set(getUserDrawCount(userId)),
					PrizeRecord.AttrDRAW_TIME.set(new Date()),
					PrizeRecord.AttrPRIZE_ID.set(bingoPrize.PRIZE_ID),
					PrizeRecord.AttrPRIZE_NAME.set(bingoPrize.DESC_ZH),
					PrizeRecord.AttrUSER_ID.set(userId+""),
					PrizeRecord.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(userId)));
			
			bingoPrize = drawPrize();
			bingoMap.put(userId, bingoPrize);
		}
		return bingoPrize;
	}
	
	private static PrizeConfig drawPrize(){
		Set<Integer> prizeIds = prizeMap.keySet();
		Random r = new SecureRandom();
		int prizeIdx = r.nextInt(prizeIds.size());//0至size()-1
		
		int index = 0;
		int bingoId = 1;
		for(Integer prizeId : prizeIds){
			if(prizeIdx == index){
				bingoId = prizeId;
			}
			index++;
		}
		
		return prizeMap.get(bingoId);
	}
}
