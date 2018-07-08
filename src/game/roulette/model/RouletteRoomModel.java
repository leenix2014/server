package game.roulette.model;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.mozat.morange.dbcache.tables.CoinBill;
import com.mozat.morange.dbcache.tables.Roulette;
import com.mozat.morange.dbcache.tables.RouletteBet;
import com.mozat.morange.dbcache.tables.RouletteBetSum;
import com.mozat.morange.dbcache.tables.RouletteRoom;
import com.mozat.morange.dbcache.tables.TUsers;

import game.user.Users;
import netty.GameModels.UserMgr;
import netty.util.MathUtil;
import netty.util.StringUtil;

public class RouletteRoomModel {
	
	public static final String READY_STAGE = "readying";
	public static final String BET_STAGE = "betting";
	public static final String PRIZE_STAGE = "prizing";
	public static final String PRIZED_STAGE = "prized";
	
	private static final int interval = 1000;
	
	private RouletteRoom config;
	private Set<Integer> admins = new HashSet<>();
	private int rouletteId;
	private int result = -1;
	private Set<String> winTargets = new HashSet<String>();
    private String stage = "";
    private int countDown;
    private Set<Integer> users = Collections.synchronizedSet(new HashSet<>());
    private Set<Integer> leavingUser = Collections.synchronizedSet(new HashSet<Integer>());
    private Set<Integer> offlineUser = Collections.synchronizedSet(new HashSet<Integer>());
    private Set<Integer> silentUsers = Collections.synchronizedSet(new HashSet<Integer>());//被禁言用户
    // Map<userId, Map<beton, count>>
    private Map<Integer, Map<String, Integer>> usersBet = new ConcurrentHashMap<>();
    // Map<userId, historyBetTotal>, 个人玩家历史下注汇总
    private Map<Integer, Integer> historyBet = new ConcurrentHashMap<>();
    // Map<beton, betTotal>, 多个玩家对同一个下注目标的汇总
    private Map<String, Integer> usersBetTotal = new ConcurrentHashMap<>();
    private SecureRandom rand = new SecureRandom();
    // Map<userId, Map<beton, reward>>
    private Map<Integer, Map<String, Integer>> usersReward = new ConcurrentHashMap<>();
    // Map<userId, historyRewardTotal>, 个人玩家历史获奖汇总
    private Map<Integer, Integer> historyReward = new ConcurrentHashMap<>();
    private boolean gameStarted = false;
	
	public RouletteRoomModel(RouletteRoom config){
		this.setConfig(config);
	}
    
    public void join(int userId){
    	synchronized (users) {
    		if(users.isEmpty() && !gameStarted){
        		readyCountDown();
        	}
        	users.add(userId);
		}
    	leavingUser.remove(userId);
    }
    
    public boolean leave(int userId){
    	Map<String, Integer> userBet = usersBet.get(userId);
    	if(userBet != null && !userBet.isEmpty()){
    		leavingUser.add(userId);
    		return false;
    	} else {
    		synchronized (users) {
				users.remove(userId);
			}
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
				CoinBill.AttrSOURCE.set("roulette"),
				CoinBill.AttrSOURCE_ID.set(config.GAME_TYPE+""),
				CoinBill.AttrSOURCE_NAME.set("Roulette下注"),
				CoinBill.AttrAMOUNT.set(-totalCoin),
				CoinBill.AttrBEFORE_BAL.set(oldCoin),
				CoinBill.AttrAFTER_BAL.set(nowCoin),
				CoinBill.AttrCREATE_TIME.set(now));
    }
    
    public void readyCountDown(){
    	synchronized (stage) {
    		stage = READY_STAGE;
		}
    	countDown = config.READY_TIME * 1000;
		result = -1;
		if(countDown <= 0){
			betCountDown();
		} else {
			RouletteRoomService.notifyStage(this);
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if(!READY_STAGE.equals(stage)){
						// 已进入下一阶段，此定时器作废
						timer.cancel();
					} else {
						countDown = countDown - interval;
						if(countDown <= 0){
							timer.cancel();
							betCountDown();
						}
					}
				}
			}, interval, interval);
		}
	}
    
	protected void betCountDown(){
		gameStarted = true;
		synchronized (stage) {
			stage = BET_STAGE;
		}
		countDown = config.BET_TIME * 1000;
		RouletteRoomService.notifyStage(this);
		Roulette rou = Roulette.create(Roulette.AttrBEGIN_BET_TIME.set(new Date()),
				Roulette.AttrBET_TOTAL.set(0),
				Roulette.AttrEND_BET_TIME.set(new Date(0)),
				Roulette.AttrRESULT.set(-1),
				Roulette.AttrREWARD_TIME.set(new Date(0)),
				Roulette.AttrREWARD_TOTAL.set(0),
				Roulette.AttrROOM_ID.set(config.ROOM_ID));
		rouletteId = rou.ID;
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
		
		Roulette.updateByCriteria(Roulette.valueList(
				Roulette.AttrEND_BET_TIME.set(new Date()), 
				Roulette.AttrBET_TOTAL.set(total)),
				Roulette.AttrID.eq(rouletteId));
		
		prizeCountDown();
	}
	
	public void prizeCountDown(){
		synchronized (stage) {
			stage = PRIZE_STAGE;
		}
		countDown = config.PRIZE_TIME * 1000;
		openPrize();
		RouletteRoomService.notifyStage(this);
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
						prizeTimeOut();
					}
				}
			}
		}, interval, interval);
	}
	
	private void openPrize(){
		result = rand.nextInt(config.GAME_TYPE);
	}
	
	private void prizeTimeOut(){
		int totalReward = 0;
		for(int userId : users){
			TUsers user = Users.load(userId);
			if(user == null){
				continue;
			}
			Map<String, Integer> userBets = getUserBet(userId);
			Map<String, Integer> betRewards = RouletteLogic.getBetResult(config.GAME_TYPE, result, userBets);
			winTargets = RouletteLogic.getWinTargets(config.GAME_TYPE, result);
			usersReward.put(userId, betRewards);
			
			int historyTotal = getHistoryReward(userId);
			Date now = new Date();
			int totalUserBet = 0; // 本次下注总额
			int totalUserReward = 0;//本次获奖总金额
			// 记录用户的下注和reward信息
			String userName = user.NAME;
			for(Map.Entry<String, Integer> userBet : userBets.entrySet()){
				int coins = userBet.getValue();
				String target = userBet.getKey();
				int reward = betRewards.get(target);
				totalUserBet += coins;
				totalUserReward += reward;
				historyTotal += reward;
				RouletteBet.create(RouletteBet.AttrBET_COUNT.set(coins),
						RouletteBet.AttrBET_ON.set(target),
						RouletteBet.AttrBET_TIME.set(now),
						RouletteBet.AttrREWARD.set(reward),
						RouletteBet.AttrROULETTE_ID.set(rouletteId),
						RouletteBet.AttrROOM_ID.set(config.ROOM_ID),
						RouletteBet.AttrUSER_ID.set(userId),
						RouletteBet.AttrUSER_NAME.set(userName));
			}
			historyReward.put(userId, historyTotal);
			if(totalUserBet > 0){
				RouletteBetSum.create(RouletteBetSum.AttrBET_TOTAL.set(totalUserBet),
						RouletteBetSum.AttrRECORD_TIME.set(now),
						RouletteBetSum.AttrRESULT.set(result),
						RouletteBetSum.AttrREWARD_TOTAL.set(totalUserReward),
						RouletteBetSum.AttrROOM_ID.set(config.ROOM_ID),
						RouletteBetSum.AttrROULETTE_ID.set(rouletteId),
						RouletteBetSum.AttrUSER_ID.set(userId),
						RouletteBetSum.AttrUSER_NAME.set(userName));
			}
			
			if(totalUserReward > 0){
				totalReward += totalUserReward;
				// 发放获奖金币
		    	int oldCoin = user.COIN_COUNT;
		    	int nowCoin = oldCoin + totalUserReward;
		    	user.COIN_COUNT = nowCoin;
		    	user.WIN_COUNT = user.WIN_COUNT + totalUserReward;
		    	user.update();
		    	CoinBill.create(CoinBill.AttrUSER_ID.set(userId+""),
						CoinBill.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(userId)),
						CoinBill.AttrSOURCE.set("roulette"),
						CoinBill.AttrSOURCE_ID.set(config.GAME_TYPE+""),
						CoinBill.AttrSOURCE_NAME.set("Roulette中奖"),
						CoinBill.AttrAMOUNT.set(totalUserReward),
						CoinBill.AttrBEFORE_BAL.set(oldCoin),
						CoinBill.AttrAFTER_BAL.set(nowCoin),
						CoinBill.AttrCREATE_TIME.set(now));
			}
			RouletteRoomService.tellResult(userId);
		}
		
		Roulette.updateByCriteria(Roulette.valueList(
				Roulette.AttrREWARD_TIME.set(new Date()), 
				Roulette.AttrREWARD_TOTAL.set(totalReward),
				Roulette.AttrRESULT.set(result)),
				Roulette.AttrID.eq(rouletteId));
		
		overCountDown();
	}
	
	protected void overCountDown(){
		synchronized (stage) {
			stage = PRIZED_STAGE;
		}
		this.countDown = config.PRIZED_TIME * 1000;
		RouletteRoomService.notifyStage(this);
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
						nextRoulette();
					}
				}
			}
		}, interval, interval);
	}
	
	protected void nextRoulette(){
		gameStarted = false;
		usersBet.clear();
		usersBetTotal.clear();
		usersReward.clear();
		//如果有用户在线，开始下一轮
		if(!isAllOffline()){
			readyCountDown();
		}
	}
	
	private boolean isAllOffline(){
		synchronized (users) {
			if(users == null || users.isEmpty()){
				return true;
			}
			Iterator<Integer> iter = users.iterator();
			while(iter.hasNext()){
				int userId = iter.next();
	    		if(leavingUser.contains(userId) || offlineUser.contains(userId)){
	    			iter.remove();
	    			RouletteRoomService.notifySelfLeave(userId);
	    			if(leavingUser.contains(userId)){//主动离开才清空累计下注和累计获奖
	    				historyBet.remove(userId);
	    				historyReward.remove(userId);
	    			}
	    		}
	    	}
			return users.isEmpty();
		}
    }

	public RouletteRoom getConfig() {
		return config;
	}

	public void setConfig(RouletteRoom config) {
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
		return users.contains(userId);
	}
	
	public Set<Integer> getRoomUser(){
		return users;
	}
	
	public Set<Integer> getRoomUserExcept(int userId){
		Set<Integer> set = new HashSet<Integer>();
		set.addAll(users);
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
	
	public Map<String, Integer> getUserReward(int userId){
    	Map<String, Integer> myReward = usersReward.get(userId);
    	if(myReward == null){
    		myReward = new ConcurrentHashMap<>();
    		usersReward.put(userId, myReward);
    	}
    	return myReward;
	}
	
	public int getResult(){
		return result;
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
	
	public boolean isAdmin(int userId){
		return admins.contains(userId);
	}

	public Set<String> getWinTargets() {
		return winTargets;
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
