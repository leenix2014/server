package game.baccarat.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.mozat.morange.dbcache.tables.Baccarat;
import com.mozat.morange.dbcache.tables.BaccaratBet;
import com.mozat.morange.dbcache.tables.BaccaratRoom;
import com.mozat.morange.dbcache.tables.BaccaratWaybill;
import com.mozat.morange.dbcache.tables.CoinBill;
import com.mozat.morange.dbcache.tables.TUsers;

import game.common.Poker;
import game.common.Pokers;
import game.user.Users;
import netty.GameModels.UserMgr;
import netty.util.MathUtil;
import netty.util.StringUtil;
import protocols.baccarat.common.beton;

public class BaccaratRoomModel {
	
	public static final String BET_STAGE = "betting";
	public static final String PRIZE_STAGE = "prizing";
	public static final String PRIZED_STAGE = "prized";
	
	private static final int interval = 1000;
	
	private BaccaratRoom config;
	private Set<Integer> admins = new HashSet<>();
	private int baccaratId;
	private Waybill way = new Waybill();
    private String stage = "";
    private int countDown;
    // Map<userId, seatId>
    private Map<Integer, Integer> seatedUsers = new ConcurrentHashMap<>();
    private Set<Integer> users = Collections.synchronizedSet(new HashSet<>());
    private Set<Integer> leavingUser = Collections.synchronizedSet(new HashSet<Integer>());
    private Set<Integer> offlineUser = Collections.synchronizedSet(new HashSet<Integer>());
    private Set<Integer> silentUsers = Collections.synchronizedSet(new HashSet<Integer>());//被禁言用户
    // Map<userId, Map<beton, lastBet>>
    private Map<Integer, Map<String, Integer>> lastUsersBet = new ConcurrentHashMap<>();
    // Map<userId, Map<beton, count>>
    private Map<Integer, Map<String, Integer>> usersBet = new ConcurrentHashMap<>();
    // Map<userId, historyBetTotal>, 个人玩家历史下注汇总
    private Map<Integer, Integer> historyBet = new ConcurrentHashMap<>();
    // Map<beton, betTotal>, 多个玩家对同一个下注目标的汇总
    private Map<String, Integer> usersBetTotal = new ConcurrentHashMap<>();
    // Map<userId, Map<beton, lastReward>>
    private Map<Integer, Map<String, Integer>> lastUsersReward = new ConcurrentHashMap<>();
    // Map<userId, Map<beton, reward>>
    private Map<Integer, Map<String, Integer>> usersReward = new ConcurrentHashMap<>();
    // Map<userId, rewardTotal>>
    private Map<Integer, Integer> rewardTotal = new ConcurrentHashMap<>();
    // Map<userId, historyRewardTotal>, 个人玩家历史获奖汇总
    private Map<Integer, Integer> historyReward = new ConcurrentHashMap<>();
    private boolean gameStarted = false;
    private Pokers cards = new Pokers(8);
    private Vector<Integer> playerHands = new Vector<>();
    private Vector<Integer> bankerHands = new Vector<>();
    private int player2Point;
    private int banker2Point;
    private int player3Point = -1;
    private int banker3Point = -1;
    private Set<String> winTargets = Collections.synchronizedSet(new HashSet<String>());
    private static Map<String, Double> peilv = new HashMap<>();
    static {
    	peilv.put(beton.banker_pair.name(), 12d);
    	peilv.put(beton.player_pair.name(), 12d);
    	peilv.put(beton.tie.name(), 9d);
    	peilv.put(beton.banker_win.name(), 1.95);
    	peilv.put(beton.player_win.name(), 2d);
    }
	
	public BaccaratRoomModel(BaccaratRoom config){
		this.setConfig(config);
	}
    
    public void join(int userId){
		int seatId = getSeatId(userId);
		if(seatId != 0){
			return;
		}
		int userCoin = UserMgr.getInstance().getUserCoin(userId);
		boolean canTakeSeat = userCoin > Math.max(config.TAKE_SEAT_COIN, 10000);
		if(!canTakeSeat){
			users.add(userId);
		} else {
			boolean hasSeat = false;
			for(int i=1;i<=9;i++){
				if(!seatedUsers.containsValue(i)){
					seatedUsers.put(userId, i);
					hasSeat = true;
					break;
				}
			}
			if(!hasSeat){
				users.add(userId);
			}
		}
		synchronized (stage) {
			if(!gameStarted){
	    		betCountDown();
	    	}
		}
    	leavingUser.remove(userId);
    }
    
    public boolean leave(int userId){
    	Map<String, Integer> userBet = usersBet.get(userId);
    	if(userBet != null && !userBet.isEmpty()){
    		leavingUser.add(userId);
    		return false;
    	} else {
			seatedUsers.remove(userId);
			users.remove(userId);
    		return true;
    	}
    }
    
    public void offline(int userId){
    	offlineUser.add(userId);
    }
    
    public void onlineAgain(int userId){
    	offlineUser.remove(userId);
	}
    
    public void addUserBet(int userId, Map<String, Integer> inbets){
    	Map<String, Integer> mybet = getUserBet(userId);
    	int totalCoin = 0;
    	int historyTotal = getHistoryBet(userId);
    	for(Map.Entry<String, Integer> inbet : inbets.entrySet()){
    		String target = inbet.getKey();
    		int coins = inbet.getValue();
    		historyTotal += coins;
    		totalCoin += coins;
    		Integer lastbet = mybet.get(target);
    		if(lastbet == null){
    			mybet.put(target, coins);
    		} else {
    			mybet.put(target, lastbet + coins);
    		}
    		Integer lastTotalBet = usersBetTotal.get(target);
    		if(lastTotalBet == null){
    			usersBetTotal.put(target, coins);
    		} else {
    			usersBetTotal.put(target, lastTotalBet + coins);
    		}
    	}
    	historyBet.put(userId, historyTotal);
    	
    	Date now = new Date();
    	int oldCoin = UserMgr.getInstance().getUserCoin(userId);
    	int nowCoin = oldCoin - totalCoin;
    	UserMgr.getInstance().setUserCoin(userId, nowCoin);
    	CoinBill.create(CoinBill.AttrUSER_ID.set(userId+""),
				CoinBill.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(userId)),
				CoinBill.AttrSOURCE.set("baccarat"),
				CoinBill.AttrSOURCE_ID.set(""),
				CoinBill.AttrSOURCE_NAME.set("Baccarat下注"),
				CoinBill.AttrAMOUNT.set(-totalCoin),
				CoinBill.AttrBEFORE_BAL.set(oldCoin),
				CoinBill.AttrAFTER_BAL.set(nowCoin),
				CoinBill.AttrCREATE_TIME.set(now));
    }
    
	protected void betCountDown(){
		gameStarted = true;
		synchronized (stage) {
			stage = BET_STAGE;
		}
		countDown = config.BET_TIME * 1000;
		BaccaratRoomService.notifyStage(this);
		Baccarat rou = Baccarat.create(Baccarat.AttrBEGIN_BET_TIME.set(new Date()),
				Baccarat.AttrBET_TOTAL.set(0),
				Baccarat.AttrEND_BET_TIME.set(new Date(0)),
				Baccarat.AttrREWARD_TIME.set(new Date(0)),
				Baccarat.AttrREWARD_TOTAL.set(0),
				Baccarat.AttrROOM_ID.set(config.ROOM_ID));
		baccaratId = rou.ID;
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(!BET_STAGE.equals(stage)){
					// 已进入下一阶段，此定时器作废
					timer.cancel();
				} else {
					countDown = countDown - interval;
					if(countDown <= 0){
						timer.cancel();
						betTimeOut();
					}
				}
			}
		}, interval, interval);
	}
	
	private void betTimeOut(){
		int total = 0;
		for(Integer bet: usersBetTotal.values()){
			total += bet;
		}
		
		Baccarat.updateByCriteria(Baccarat.valueList(
				Baccarat.AttrEND_BET_TIME.set(new Date()), 
				Baccarat.AttrBET_TOTAL.set(total)),
				Baccarat.AttrID.eq(baccaratId));
		
		prizeCountDown();
	}
	
	public void prizeCountDown(){
		synchronized (stage) {
			stage = PRIZE_STAGE;
		}
		countDown = 10 * 1000;
		openPrize();//开奖并记录路单
		BaccaratRoomService.notifyStage(this);
		if(cards.isNeedShuffle()){
			cards.shuffle();
			countDown += 5000;
			BaccaratWaybill.create(BaccaratWaybill.AttrROOM_ID.set(config.ROOM_ID),
					BaccaratWaybill.AttrLAST_BACC_ID.set(baccaratId),
					BaccaratWaybill.AttrSIZE.set(way.size()),
					BaccaratWaybill.AttrMAIN_ROAD.set(way.getMainRoadString()),
					BaccaratWaybill.AttrDISH_ROAD.set(way.getDishRoadString()),
					BaccaratWaybill.AttrRECORD_TIME.set(new Date()));
		}
		calReward();//计算奖励并通知用户
		if(cards.isNeedShuffle()){
			way.clear();
			cards.setNeedShuffle(false);
		}
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(!PRIZE_STAGE.equals(stage)){
					// 已进入下一阶段，此定时器作废
					timer.cancel();
				} else {
					countDown = countDown - interval;
					if(countDown <= 0){
						timer.cancel();
						overCountDown();
					}
				}
			}
		}, interval, interval);
	}
	
	private void openPrize(){
		int pivot = 0;
		playerHands.add(cards.deal());
		bankerHands.add(cards.deal());
		playerHands.add(cards.deal());
		bankerHands.add(cards.deal());
		player2Point = BaccaratLogic.getPoint(playerHands);
		banker2Point = BaccaratLogic.getPoint(bankerHands);
		if(BaccaratLogic.isPair(playerHands)){
			winTargets.add(beton.player_pair.name());
			pivot += 6;
		}
		if(BaccaratLogic.isPair(bankerHands)){
			winTargets.add(beton.banker_pair.name());
			pivot += 3;
		}
		// 出现天牌
		if(BaccaratLogic.isSkyPoint(banker2Point) || BaccaratLogic.isSkyPoint(player2Point)){
			pivot += 12;
			if(banker2Point < player2Point){
				winTargets.add(beton.player_win.name());
				pivot += 2;
			} else if(banker2Point == player2Point){
				winTargets.add(beton.tie.name());
				pivot += 3;
			} else {
				winTargets.add(beton.banker_win.name());
				pivot += 1;
			}
			way.accept(pivot);
			return;
		}
		Poker player3card = null;
		int playerPoint = player2Point;
		if(BaccaratLogic.isPlayerMustNeedCard(player2Point)){
			int player3 = cards.deal();
			player3card = Poker.getPoker(player3);
			playerHands.add(player3);
			player3Point = BaccaratLogic.getPoint(playerHands);
			playerPoint = player3Point;
			countDown = countDown + 5000;
		}
		boolean bankerNeedCard;
		if(BaccaratLogic.isBankerMustNeedCard(banker2Point)){
			bankerNeedCard = true;
		} else if(banker2Point == 3 && (player3card == null || player3card.getWeight() != 8)){
			bankerNeedCard = true;
		} else if(banker2Point == 4 && (player3card == null || 
				(player3card.getWeight() != 1 && player3card.getWeight() != 8 && player3card.getWeight() != 9 && player3card.getWeight() != 0))){
			bankerNeedCard = true;
		} else if(banker2Point == 5 && (player3card == null || 
				(player3card.getWeight() == 4 || player3card.getWeight() == 5 || player3card.getWeight() == 6 || player3card.getWeight() == 7))){
			bankerNeedCard = true;
		} else if(banker2Point == 6 && (player3card != null && (player3card.getWeight() == 6 || player3card.getWeight() == 7))){
			bankerNeedCard = true;
		} else {
			bankerNeedCard = false;
		}
		
		int bankerPoint = banker2Point;
		if(bankerNeedCard){
			bankerHands.add(cards.deal());
			banker3Point = BaccaratLogic.getPoint(bankerHands);
			bankerPoint = banker3Point;
			countDown = countDown + 5000;
		}
		
		if(bankerPoint < playerPoint){
			winTargets.add(beton.player_win.name());
			pivot += 2;
		} else if(bankerPoint == playerPoint){
			winTargets.add(beton.tie.name());
			pivot += 3;
		} else {
			winTargets.add(beton.banker_win.name());
			pivot += 1;
		}
		way.accept(pivot);
	}
	
	private void calReward(){
		int totalReward = 0;//所有玩家总奖励
		for(int userId : getRoomUser()){
			TUsers user = Users.load(userId);
			if(user == null){
				continue;
			}
			Map<String, Integer> userBets = getUserBet(userId);
			Map<String, Integer> betRewards = new HashMap<>();
			for(Map.Entry<String, Integer> userBet : userBets.entrySet()){
				String target = userBet.getKey();
				int coins = userBet.getValue();
				if(winTargets.contains(target)){
					betRewards.put(target, (int) Math.round(coins * peilv.get(target)));
				} else {
					betRewards.put(target, 0);
				}
				
				// 和局返还庄闲下注
				if(winTargets.contains("tie") && ("player_win".equals(target) || "banker_win".equals(target))){
					betRewards.put(target, coins);
				}
			}
			usersReward.put(userId, betRewards);
			
			int historyTotal = getHistoryReward(userId);
			Date now = new Date();
			int totalCoin = 0;//单个玩家获奖总金额
			// 记录用户的下注和reward信息
			for(Map.Entry<String, Integer> userBet : userBets.entrySet()){
				int coins = userBet.getValue();
				String target = userBet.getKey();
				int reward = betRewards.get(target);
				totalCoin += reward;
				historyTotal += reward;
				BaccaratBet.create(BaccaratBet.AttrBET_COUNT.set(coins),
						BaccaratBet.AttrBET_ON.set(target),
						BaccaratBet.AttrBET_TIME.set(now),
						BaccaratBet.AttrREWARD.set(reward),
						BaccaratBet.AttrBACCARAT_ID.set(baccaratId),
						BaccaratBet.AttrROOM_ID.set(config.ROOM_ID),
						BaccaratBet.AttrUSER_ID.set(userId),
						BaccaratBet.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(userId)));
			}
			historyReward.put(userId, historyTotal);
			rewardTotal.put(userId, totalCoin);
			
			if(totalCoin > 0){
				totalReward += totalCoin;
				// 发放获奖金币
		    	int oldCoin = user.COIN_COUNT;
		    	int nowCoin = oldCoin + totalCoin;
		    	user.COIN_COUNT = nowCoin;
		    	user.WIN_COUNT = user.WIN_COUNT + totalCoin;
		    	user.update();
		    	CoinBill.create(CoinBill.AttrUSER_ID.set(userId+""),
						CoinBill.AttrUSER_NAME.set(user.NAME),
						CoinBill.AttrSOURCE.set("baccarat"),
						CoinBill.AttrSOURCE_ID.set(config.ROOM_ID),
						CoinBill.AttrSOURCE_NAME.set("Baccarat中奖"),
						CoinBill.AttrAMOUNT.set(totalCoin),
						CoinBill.AttrBEFORE_BAL.set(oldCoin),
						CoinBill.AttrAFTER_BAL.set(nowCoin),
						CoinBill.AttrCREATE_TIME.set(now));
			}
		}
		
		// 计算完结果才通知
		for(int userId : getRoomUser()){
			BaccaratRoomService.tellResult(userId);
		}
		
		Baccarat.updateByCriteria(Baccarat.valueList(
				Baccarat.AttrREWARD_TIME.set(new Date()), 
				Baccarat.AttrREWARD_TOTAL.set(totalReward),
				Baccarat.AttrPLAYER_CARDS.set(playerHands.toString()),
				Baccarat.AttrBANKER_CARDS.set(bankerHands.toString()),
				Baccarat.AttrBANKER_2POINT.set(banker2Point),
				Baccarat.AttrBANKER_3POINT.set(banker3Point),
				Baccarat.AttrPLAYER_2POINT.set(player2Point),
				Baccarat.AttrPLAYER_3POINT.set(player3Point),
				Baccarat.AttrWIN_TARGETS.set(winTargets.toString())),
				Baccarat.AttrID.eq(baccaratId));
	}
	
	protected void overCountDown(){
		synchronized (stage) {
			stage = PRIZED_STAGE;
		}
		this.countDown = config.OVER_TIME * 1000;
		BaccaratRoomService.notifyStage(this);
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(!PRIZED_STAGE.equals(stage)){
					// 已进入下一阶段，此定时器作废
					timer.cancel();
				} else {
					countDown = countDown - interval;
					if(countDown <= 0){
						timer.cancel();
						nextBaccarat();
					}
				}
			}
		}, interval, interval);
	}
	
	protected void nextBaccarat(){
		playerHands.clear();
		bankerHands.clear();
		player2Point = 0;
		banker2Point = 0;
		player3Point = -1;
		banker3Point = -1;
		winTargets.clear();
		
		lastUsersBet.clear();
		lastUsersReward.clear();
		lastUsersBet.putAll(usersBet);
		lastUsersReward.putAll(usersReward);
		usersBet.clear();
		usersBetTotal.clear();
		usersReward.clear();
		rewardTotal.clear();
		synchronized (stage) {
			gameStarted = false;
			//如果有用户在线，开始下一轮
			if(!isAllOffline()){
				betCountDown();
			}
		}
	}
	
	private boolean isAllOffline(){
		Set<Integer> user = getRoomUser();
		if(user == null || user.isEmpty()){
			return true;
		}
		Iterator<Integer> iter = user.iterator();
		while(iter.hasNext()){
			int userId = iter.next();
    		if(leavingUser.contains(userId) || offlineUser.contains(userId)){
    			iter.remove();
    			seatedUsers.remove(userId);
    			users.remove(userId);
    			BaccaratRoomService.notifySelfLeave(userId);
    			if(leavingUser.contains(userId)){//主动离开才清空累计下注和累计获奖
    				historyBet.remove(userId);
    				historyReward.remove(userId);
    			}
    		}
    	}
		return user.isEmpty();
    }

	public BaccaratRoom getConfig() {
		return config;
	}

	public void setConfig(BaccaratRoom config) {
		this.config = config;
		admins.clear();
		if(config.ADMIN_IDS != null && !StringUtil.isEmpty(config.ADMIN_IDS.trim())){
			String[] parts = config.ADMIN_IDS.trim().split(",");
			for(String part : parts){
				part = part.trim();
				int admin = MathUtil.parseInt(part);
				if(admin != 0) {
					admins.add(admin);
				}
			}
		}
	}

	public Integer getCountDown() {
		return countDown;
	}

	public String getStage() {
		return stage;
	}
	
	public boolean isInRoom(int userId){
		return users.contains(userId) || seatedUsers.containsKey(userId);
	}
	
	public Set<Integer> getNonSeatedUser(){
		return users;
	}
	
	public Set<Integer> getSeatedUser(){
		return seatedUsers.keySet();
	}
	
	public Set<Integer> getRoomUser(){
		Set<Integer> set = new HashSet<>();
		set.addAll(seatedUsers.keySet());
		set.addAll(users);
		return set;
	}
	
	public Set<Integer> getRoomUserExcept(int userId){
		Set<Integer> set = getRoomUser();
		set.remove(userId);
		return set;
	}
	
	public String getRoomId(){
		return config.ROOM_ID;
	}
	
	public Map<String, Integer> getUserBet(int userId){
    	Map<String, Integer> mybet = usersBet.get(userId);
    	if(mybet == null){
    		mybet = new ConcurrentHashMap<>();
    		usersBet.put(userId, mybet);
    	}
    	return mybet;
	}
	
	public int getBetTotal(int userId){
		int total = 0;
		for(Integer coin : getUserBet(userId).values()){
			total += coin;
		}
		return total;
	}
	
	public boolean isBigPlayer(int userId){
		return getBetTotal(userId) > 1000;
	}
	
	public Map<String, Integer> getUserReward(int userId){
    	Map<String, Integer> myReward = usersReward.get(userId);
    	if(myReward == null){
    		myReward = new ConcurrentHashMap<>();
    		usersReward.put(userId, myReward);
    	}
    	return myReward;
	}
	
	public boolean isBigWinner(int userId){
		return getRewardTotal(userId) > 1000;
	}
	
	public Map<String, Integer> getLastBet(int userId){
    	Map<String, Integer> myBet = lastUsersBet.get(userId);
    	if(myBet == null){
    		myBet = new HashMap<>();
    	}
    	return myBet;
	}
	
	public Map<String, Integer> getLastReward(int userId){
    	Map<String, Integer> myReward = lastUsersReward.get(userId);
    	if(myReward == null){
    		myReward = new HashMap<>();
    	}
    	return myReward;
	}
	
	public int getSeatId(int userId){
		Integer seatId = seatedUsers.get(userId);
		if(seatId == null){
			return 0;
		}
		return seatId;
	}
	
	public Map<String, Integer> getTotalBet(){
		return usersBetTotal;
	}
	
	public int getHistoryBet(int userId){
		Integer historyTotal = historyBet.get(userId);
		if(historyTotal == null){
			return 0;
		}
		return historyTotal;
	}
	
	public int getHistoryReward(int userId){
		Integer historyTotal = historyReward.get(userId);
		if(historyTotal == null){
			return 0;
		}
		return historyTotal;
	}

	public Vector<Integer> getPlayerHands() {
		return playerHands;
	}

	public Vector<Integer> getBankerHands() {
		return bankerHands;
	}

	public int getPlayer2Point() {
		return player2Point;
	}

	public int getBanker2Point() {
		return banker2Point;
	}

	public int getPlayer3Point() {
		return player3Point;
	}

	public int getBanker3Point() {
		return banker3Point;
	}

	public Set<String> getWinTargets() {
		return winTargets;
	}
	
	public int getRemains(){
		return cards.getRemains();
	}
	
	public int getPlayerCount(){
		return users.size() + seatedUsers.size();
	}
	
	public int getRewardTotal(int userId){
		Integer total = rewardTotal.get(userId);
		if(total == null){
			return 0;
		}
		return total;
	}
	
	public boolean isAdmin(int userId){
		return admins.contains(userId);
	}
	
	public Waybill getWaybill(){
		return way;
	}
	
	public boolean isNeedShuffle(){
		return cards.isNeedShuffle();
	}
	
	public void silent(int userId){
		silentUsers.add(userId);
	}
	
	public void unsilent(int userId){
		silentUsers.remove(userId);
	}
	
	public boolean isSilent(int userId){
		return silentUsers.contains(userId);
	}
}
