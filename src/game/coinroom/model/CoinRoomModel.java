package game.coinroom.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.dbcache.tables.CoinBill;
import com.mozat.morange.dbcache.tables.CoinRoom;
import com.mozat.morange.dbcache.tables.CoinRoomHistory;
import com.mozat.morange.dbcache.tables.TUsers;

import game.session.SessionManager;
import game.user.Users;
import netty.GameEvaluation.ThreeCardsLogic;
import netty.GameModels.UserMgr;
import protocols.coinroom.common.room_stage;
import protocols.coinroom.common.room_type;
import protocols.coinroom.common.user_role;

public class CoinRoomModel {
	private static Logger logger = LoggerFactory.getLogger(CoinRoomModel.class);
	
	private static Map<room_stage, Integer> stageTime = new HashMap<room_stage, Integer>();
	private static final int interval = 1000;
	static {
		stageTime.put(room_stage.ready_ing, 5000);
		stageTime.put(room_stage.start_ing, 2000);
		stageTime.put(room_stage.grab_ing, 10000);
		stageTime.put(room_stage.confirm_banker_ing, 2000);
		stageTime.put(room_stage.bet_ing, 15000);
		stageTime.put(room_stage.showhand_ing, 15000);
		stageTime.put(room_stage.over_ing, 10000);
	}

	private CoinRoom config;
	private room_stage stage;
	private int countDown;
	// Map<userId, seatId>
	private Map<Integer, Integer> playerSeat = new ConcurrentHashMap<Integer, Integer>();
	// Map<userId, seatId>
	private Map<Integer, Integer> sitdownUser = new ConcurrentHashMap<Integer, Integer>();
	private Set<Integer> audiences = Collections.synchronizedSet(new HashSet<Integer>());
	private Set<Integer> readyUser = Collections.synchronizedSet(new HashSet<Integer>());
	private Set<Integer> leavingUser = Collections.synchronizedSet(new HashSet<Integer>());
	// Map<userId, grab>
	private Map<Integer, Integer> playerGrab = new ConcurrentHashMap<Integer, Integer>();
	// Map<userId, bet>
	private Map<Integer, Integer> playerBet = new ConcurrentHashMap<Integer, Integer>();
	// Map<userId, cards>
	private Map<Integer, List<Integer>> playerCards = new ConcurrentHashMap<Integer, List<Integer>>();
	// Map<userId, cards>
	private Map<Integer, List<Integer>> visuableCards = new ConcurrentHashMap<Integer, List<Integer>>();
	// Map<userId, cardType>
	private Map<Integer, Integer> playerCardType = new ConcurrentHashMap<Integer, Integer>();
	// Map<userId, score>
	private Map<Integer, Integer> playerScore = new ConcurrentHashMap<Integer, Integer>();
	// Map<userId, draw>
	private Map<Integer, Integer> playerDraw = new ConcurrentHashMap<Integer, Integer>();
	protected ThreeCardsLogic logic = new ThreeCardsLogic();
	private DealPhase currPhase = DealPhase.Phase1;
	private String gameId = "";
	private boolean gameStarted = false;
	private Integer banker;
	private Integer maxGrab;
	public static enum DealPhase {
		Phase1(3), Phase2(2);
		DealPhase(int dealNum){
			this.dealNum = dealNum;
		}
		private int dealNum;
		public static DealPhase nextPhase(DealPhase phase){
			if(Phase1.equals(phase)){
				return Phase2;
			}
			return Phase1;
		}
	}
	
	public CoinRoomModel(CoinRoom config){
		this.config = config;
		this.stage = room_stage.init;
	}
	
	/**
	 * 加入房间
	 * @param userId
	 * @return 座位号 or 0 as audience
	 */
	public int join(int userId){
		int seat = getSeatId(userId);
		if(seat > 0){
			return seat;
		}
		if(isFull()){
			audiences.add(userId);
			return 0;
		}
		for(int i=1;i<=config.MAX_SEAT;i++){
			if(!playerSeat.containsValue(i) && !sitdownUser.containsValue(i)){
				if(!gameStarted){
					playerSeat.put(userId, i);
				} else {
					sitdownUser.put(userId, i);
				}
				if(playerSeat.size() >= 2 && !gameStarted && countDown <= 0){
					readyCountDown();
				}
				return i;
			}
		}
		return 0;
	}
	
	public boolean isFull(){
		return (playerSeat.size() + sitdownUser.size()) >= config.MAX_SEAT;
	}
	
	public int getSeatId(int userId){
		Integer seat = playerSeat.get(userId);
		if(seat == null){
			seat = sitdownUser.get(userId);
		}
		if(seat == null){
			return 0;
		} else {
			return seat;
		}
	}
	
	public boolean isPlayer(int userId){
		Integer seat = playerSeat.get(userId);
		if(seat == null){
			return false;
		} else {
			return true;
		}
	}
	
	public user_role getUserRole(int userId){
		Integer seat = playerSeat.get(userId);
		if(seat != null){
			return user_role.player;
		}
		seat = sitdownUser.get(userId);
		if(seat != null){
			return user_role.sitdowner;
		} else {
			return user_role.audience;
		}
	}
	
	public void readyCountDown(){
		synchronized (stage) {
			if(stage != room_stage.init && stage != room_stage.over_ing){
				return;
			}
			this.stage = room_stage.ready_ing;
		}
		this.countDown = stageTime.get(stage);
		if(countDown > 0){
			CoinRoomService.notifyStage(this);
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if(stage != room_stage.ready_ing){
						// 已进入下一阶段，此定时器作废
						timer.cancel();
					} else {
						countDown = countDown - interval;
						if(countDown <= 0){
							timer.cancel();
							allReady();
						}
					}
				}
			}, interval, interval);
		} else {
			allReady();
		}
	}
	
	public int sitdown(int userId, int seatId){
		if(seatId < 1){
			seatId = 1;
		}
		if(seatId > config.MAX_SEAT){
			seatId = config.MAX_SEAT;
		}
		int playerId = getPlayer(seatId);
		if(playerId != 0){
			return 1; //1=此座位已有其他玩家
		}
		if(stage == room_stage.ready_ing){
			playerSeat.put(userId, seatId);
		} else {
			sitdownUser.put(userId, seatId);
		}
		return 0;
	}
	
	private int getPlayer(int seatId){
		for(Map.Entry<Integer, Integer> entry :playerSeat.entrySet()){
			if(seatId == entry.getValue()){
				return entry.getKey();
			}
		}
		return 0;
	}
	
	public boolean ready(int userId){
		if(!playerSeat.containsKey(userId)){
			return false;
		}
		readyUser.add(userId);
		if(readyUser.size() >= playerSeat.size()){
			allReady();
			return true;
		}
		return false;
	}
	
	private void allReady(){
		start();
	}
	
	private void init(){
		readyUser.clear();
		playerGrab.clear();
		playerBet.clear();
		playerCards.clear();
		playerCardType.clear();
		playerScore.clear();
		playerDraw.clear();
		logic.shuffle();
		currPhase = DealPhase.Phase1;
		gameId = UUID.randomUUID().toString();
		banker = null;
	}
	
	public void start(){
		init();//初始化
		CoinRoomService.notifyStart(this);
		gameStarted = true;
		synchronized (stage) {
			if(stage != room_stage.ready_ing){
				return;
			}
			this.stage = room_stage.start_ing;
		}
		this.countDown = stageTime.get(stage);
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(stage != room_stage.start_ing){
					// 已进入下一阶段，此定时器作废
					timer.cancel();
				} else {
					countDown = countDown - interval;
					if(countDown <= 0){
						if(room_type.dealer_none.name().equals(config.MODE)){
							deal();
						}
						if(room_type.dealer_grab.name().equals(config.MODE)){
							grabCountDown();
						}
					}
				}
			}
		}, interval, interval);
	}
	
	protected void grabCountDown(){
		this.stage = room_stage.grab_ing;
		this.countDown = stageTime.get(stage);
		CoinRoomService.notifyStage(this);
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(stage != room_stage.grab_ing){
					// 已进入下一阶段，此定时器作废
					timer.cancel();
				} else {
					countDown = countDown - interval;
					if(countDown <= 0){
						timer.cancel();
						confirmBankerCountDown();
					}
				}
			}
		}, interval, interval);
	}
	
	public void grab(int userId, int grab){
		if(!playerSeat.containsKey(userId)){
			return;
		}
		if(grab <= 0){
			grab = 1;
		}
		playerGrab.put(userId, grab);
		if(banker == null || (maxGrab != null && grab > maxGrab)){
			banker = userId;
			maxGrab = grab;
		}
		if(playerGrab.size() >= playerSeat.size()){
			confirmBankerCountDown();
		}
	}
	
	public int getGrab(int userId){
		Integer grab = playerGrab.get(userId);
		if(grab == null || grab <= 0){
			return 1;
		}
		return grab;
	}
	
	protected void confirmBankerCountDown(){
		synchronized (stage) {
			if(stage != room_stage.grab_ing){
				//logger.info("Room({}) confirm banker twice!", config.ROOM_ID);
				return;
			}
			this.stage = room_stage.confirm_banker_ing;
		}
		this.countDown = stageTime.get(stage);
		CoinRoomService.notifyStage(this);
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(stage != room_stage.confirm_banker_ing){
					// 已进入下一阶段，此定时器作废
					timer.cancel();
				} else {
					countDown = countDown - interval;
					if(countDown <= 0){
						timer.cancel();
						allGrab();
					}
				}
			}
		}, interval, interval);
	}
	
	private void allGrab(){
		CoinRoomService.notifyBanker(this);
		betCountDown();
	}
	
	public int getBanker(){
		if(banker != null){
			return banker;
		}
		if(room_type.dealer_none.name().equals(config.MODE)){
			return 0;
		}
		if(banker == null){
			//没人下注，随机拿一位玩家
			Object[] players = playerSeat.keySet().toArray();
			int index = new Random().nextInt(players.length);
			banker = (Integer)players[index];
		}
		return banker;
	}
	
	protected void betCountDown(){
		this.stage = room_stage.bet_ing;
		this.countDown = stageTime.get(stage);
		CoinRoomService.notifyStage(this);
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(stage != room_stage.bet_ing){
					// 已进入下一阶段，此定时器作废
					timer.cancel();
				} else {
					countDown = countDown - interval;
					if(countDown <= 0){
						timer.cancel();
						allBet();
					}
				}
			}
		}, interval, interval);
	}
	
	public void bet(int userId, int bet){
		if(!playerSeat.containsKey(userId)){
			return;
		}
		if(userId == getBanker()){
			bet = 1;
		}
		playerBet.put(userId, bet);
		if(playerBet.size() >= playerSeat.size()-1){
			allBet();
		}
	}
	
	public int getBet(int userId){
		Integer bet = playerBet.get(userId);
		if(bet == null || bet <= 0){
			return 1;
		}
		return bet;
	}
	
	private void allBet(){
		synchronized (stage) {
			if(stage != room_stage.bet_ing){
				//logger.info("Room({}) all bet twice!", config.ROOM_ID);
				return;
			}
			stage = room_stage.showhand_ing;
		}
		deal();
	}
	
	public void deal(){
		for(Integer playerId : playerSeat.keySet()){
			Vector<Integer> cards = logic.dealing(currPhase.dealNum);
			if(DealPhase.Phase1.equals(currPhase)){
				playerCards.put(playerId, cards);
			} else {
				//playerCards.get(playerId).addAll(cards);
				Vector<Integer> newCards = new Vector<>();
				newCards.addAll(getCards(playerId));
				newCards.addAll(cards);
				playerCards.put(playerId, newCards);
			}
		}
		CoinRoomService.notifyCards(this);
		showhandCountDown();
	}
	
	protected void showhandCountDown(){
		this.stage = room_stage.showhand_ing;
		this.countDown = stageTime.get(stage);
		CoinRoomService.notifyStage(this);
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(stage != room_stage.showhand_ing){
					// 已进入下一阶段，此定时器作废
					timer.cancel();
				} else {
					countDown = countDown - interval;
					if(countDown <= 0){
						timer.cancel();
						allShowHand();
					}
				}
			}
		}, interval, interval);
	}
	
	public boolean showhand(int userId, List<Integer> handcards){
		List<Integer> cards = playerCards.get(userId);
		if(cards == null){
			logger.info("User({}) has no handcard in server", userId);
			return false;
		}
		if(cards.size() != handcards.size()){
			logger.info("Client handcard size({}) not consist with server handcard size({})", handcards.size(), cards.size());
			return false;
		}
		for(Integer handcard : handcards){
			if(!cards.contains(handcard)){
				logger.info("Client handcard({}) not consist with server handcard({})", handcards, cards);
				return false;
			}
		}
		playerCards.put(userId, handcards);
		visuableCards.put(userId, handcards);
		playerCardType.put(userId, logic.getCardType(handcards));
		if(playerCardType.size() >= playerSeat.size()){
			allShowHand();
		}
		return true;
	}
	
	private void allShowHand(){
		synchronized (stage) {
			if(stage != room_stage.showhand_ing){
				//logger.info("Room({}) all showhand twice!", config.ROOM_ID);
				return;
			}
			stage = room_stage.over_ing;
		}
		for(Map.Entry<Integer, List<Integer>> entry :playerCards.entrySet()){
			int playerId = entry.getKey();
			List<Integer> handCards = entry.getValue();
			if(playerCardType.get(playerId) == null){
				playerCardType.put(playerId, logic.getBestCardType(handCards));
				playerCards.put(playerId, logic.getBest(handCards));
			}
		}
		CompareLogic comparer = new CompareLogic(config.BASE_SCORE, getBanker(), playerBet, playerCardType);
		if(room_type.dealer_none.name().equals(config.MODE)){
			comparer.compareToEachOther();
		}
		if(room_type.dealer_grab.name().equals(config.MODE)){
			comparer.compareToBanker();
		}
		this.playerScore = comparer.getPlayerScore();
		for(Map.Entry<Integer, Integer> entry : playerScore.entrySet()){
			int playerId = entry.getKey();
			int score = entry.getValue();
			if(score > 0){ // 赢钱的才有抽水
				int draw = Math.round((float)score/100 * config.DRAW_PERCENT);
				playerDraw.put(playerId, draw);
			} else {
				playerDraw.put(playerId, 0);//覆盖Phase1的结果
			}
		}
		for(Map.Entry<Integer, Integer> entry:playerScore.entrySet()){
			int playerId = entry.getKey();
			int score = entry.getValue();
			int draw = getDraw(playerId);
			TUsers user = Users.load(playerId);
			if(user == null){
				continue;
			}
			
			Date now = new Date();
			String userName = user.NAME;
			if(score != 0){
		    	int oldCoin = user.COIN_COUNT;
		    	int nowCoin = oldCoin + score - draw;
		    	user.COIN_COUNT = nowCoin;
		    	if(score > 0){
		    		user.WIN_COUNT = user.WIN_COUNT + score - draw;
		    	}
		    	user.update();
		    	
		    	CoinBill.create(CoinBill.AttrUSER_ID.set(playerId+""),
						CoinBill.AttrUSER_NAME.set(userName),
						CoinBill.AttrSOURCE.set("coinroom"),
						CoinBill.AttrSOURCE_ID.set(config.ROOM_ID+""),
						CoinBill.AttrSOURCE_NAME.set("金币场("+config.ROOM_ID+")"+(score>=0?"赢":"输")+"钱"),
						CoinBill.AttrAMOUNT.set(score - draw),
						CoinBill.AttrBEFORE_BAL.set(oldCoin),
						CoinBill.AttrAFTER_BAL.set(nowCoin),
						CoinBill.AttrCREATE_TIME.set(now));
			}
	    	
	    	CoinRoomHistory.create(CoinRoomHistory.AttrGAME_ID.set(gameId),
	    			CoinRoomHistory.AttrUSER_ID.set(playerId),
	    			CoinRoomHistory.AttrUSER_NAME.set(userName),
	    			CoinRoomHistory.AttrROOM_ID.set(config.ROOM_ID),
	    			CoinRoomHistory.AttrSEAT_ID.set(getSeatId(playerId)),
	    			CoinRoomHistory.AttrBET.set(getBet(playerId)),
	    			CoinRoomHistory.AttrGRAB.set(getGrab(playerId)),
	    			CoinRoomHistory.AttrPHASE.set(currPhase.name()),
	    			CoinRoomHistory.AttrCARDS.set(getCards(playerId).toString()),
	    			CoinRoomHistory.AttrCARD_TYPE.set(getCardType(playerId)),
	    			CoinRoomHistory.AttrSCORE.set(score),
	    			CoinRoomHistory.AttrDRAW.set((float) draw),
	    			CoinRoomHistory.AttrBANKER.set(room_type.dealer_none.name().equals(config.MODE)?false:getBanker() == playerId),
	    			CoinRoomHistory.AttrCREATE_TIME.set(now));
		}
		CoinRoomService.notifyOver(this);
		overCountDown();
	}
	
	public int getScore(int playerId){
		Integer score = playerScore.get(playerId);
		if(score == null){
			return 0;
		}
		return score;
	}
	
	public int getDraw(int playerId){
		Integer draw = playerDraw.get(playerId);
		if(draw == null){
			return 0;
		}
		return draw;
	}
	
	public List<Integer> getShowCards(int playerId){
		List<Integer> cards = visuableCards.get(playerId);
		if(cards == null){
			return new ArrayList<Integer>();
		}
		return cards;
	}
	
	public List<Integer> getCards(int userId){
		List<Integer> cards = playerCards.get(userId);
		if(cards == null){
			return new ArrayList<Integer>();
		}
		return cards;
	}
	
	public int getCardType(int userId){
		Integer cardType = playerCardType.get(userId);
		if(cardType == null){
			return 0;
		}
		return cardType;
	}
	
	protected void overCountDown(){
		this.stage = room_stage.over_ing;
		this.countDown = stageTime.get(stage);
		CoinRoomService.notifyStage(this);
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(stage != room_stage.over_ing){
					// 已进入下一阶段，此定时器作废
					timer.cancel();
				} else {
					countDown = countDown - interval;
					if(countDown <= 0){
						timer.cancel();
						if(DealPhase.Phase1.equals(currPhase)){
							currPhase = DealPhase.nextPhase(currPhase);
							playerCardType.clear();
							playerScore.clear();
							playerDraw.clear();
							deal();
						} else {
							over();
						}
					}
				}
			}
		}, interval, interval);
		if(DealPhase.Phase2.equals(currPhase)){
			gameStarted = false;//马上允许standup和leave
		}
	}
	
	protected void over(){
		playerSeat.putAll(sitdownUser);//坐着的玩家开始玩牌
		sitdownUser.clear();
		Iterator<Entry<Integer, Integer>> iter = playerSeat.entrySet().iterator();
		while(iter.hasNext()){
			Entry<Integer, Integer> entry = iter.next();
			Integer player = entry.getKey();
			int coin = UserMgr.getInstance().getUserCoin(player);
			if(SessionManager.isOffline(player) || leavingUser.contains(player) || coin <= config.KICK_OUT_COIN){
				iter.remove();
				CoinRoomService.notifySelfLeave(player);
				CoinRoomService.notifyLeave(this, entry.getValue());
			}
		}
		leavingUser.clear();
		if(playerSeat.size() < 2){
			stop();
		} else {
			readyCountDown();
			gameStarted = false;
		}
	}
	
	public int standup(int userId){
		int result = leave(userId);
		if(result == 0){
			audiences.add(userId);
		}
		return result;
	}
	
	public void onlineAgain(int userId){
		leavingUser.remove(userId);
	}
	
	public int leave(int userId){
		if(isPlayer(userId) && gameStarted){
			leavingUser.add(userId);
			return 1; // 1=玩完本局才能退出
		}
		
		playerSeat.remove(userId);
		sitdownUser.remove(userId);
		audiences.remove(userId);
		if(playerSeat.size() + sitdownUser.size() < 2){
			stop();
		}
		return 0;
	}
	
	public void leaveOnOver(int userId){
		leavingUser.add(userId);
	}
	
	public void stop(){
		init();
		stage = room_stage.init;
		countDown = -1;
		CoinRoomService.notifyStage(this);
		gameStarted = false;
	}

	public CoinRoom getConfig() {
		return config;
	}
	
	public void setConfig(CoinRoom config) {
		this.config = config;
	}
	
	public int getRoomId(){
		return this.config.ROOM_ID;
	}
	
	public DealPhase getPhase(){
		return currPhase;
	}

	public room_stage getStage() {
		return stage;
	}

	public int getCountDown() {
		return countDown;
	}
	
	public int getPlayerCount(){
		return playerSeat.size() + sitdownUser.size();
	}
	
	public Map<Integer, Integer> getPlayerAndSitdowner(){
		Map<Integer, Integer> allPlayer = new HashMap<>();
		allPlayer.putAll(playerSeat);
		allPlayer.putAll(sitdownUser);
		return allPlayer;
	}

	public Map<Integer, Integer> getPlayerSeat() {
		return playerSeat;
	}
	
	public Map<Integer, List<Integer>> getPlayerCards() {
		return playerCards;
	}

	public Set<Integer> getRoomUser() {
		Set<Integer> allUser = new HashSet<Integer>();
		allUser.addAll(playerSeat.keySet());
		allUser.addAll(sitdownUser.keySet());
		allUser.addAll(audiences);
		return allUser;
	}
	
	public Set<Integer> getRoomUserExcept(int userId){
		Set<Integer> users =  getRoomUser();
		users.remove(userId);
		return users;
	}
}
