package netty.GameModels;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import netty.GameCommon.DismissRoomPollingHandler;
import netty.GameCommon.GameBetHandler;
import netty.GameCommon.GameShowHandHandler;
import netty.GameCommon.GrabDealerHandler;
import netty.interf.SingleOverHandler;

public class TimerTaskMgr {
	//抢庄超时时间
	public final static int GRAB_TIMEOUT = 9000;
	//抢庄完成后到下注开始时间间隔
	public final static int DEALER_SETTLED_DELAY = 2000;
	//下注超时时间
	public final static int BET_TIMEOUT = 15000;
	//发牌后到摊牌超时时间
	public final static int SHOWHAND_TIMEOUT = 16000;
	//摊牌后到下一局开始时间间隔
	public final static int SHOWHAND_DELAY = 10000;
	//解散房间投票超时时间
	public final static int DISMISS_VOTE_TIMEOUT = 8000;
	
	volatile private static TimerTaskMgr instance = null;
    private TimerTaskMgr(){
    }

    public static TimerTaskMgr getInstance() {
        if(instance == null){
            synchronized (TimerTaskMgr.class) {
                if(instance == null){
                    instance = new TimerTaskMgr();
                }
            }
        }

        return instance;
    }
    
    private Map<Integer, Timer> timeTaskMap = new HashMap<Integer, Timer>();
    
    public void genTimer(int roomID) {
    	synchronized (TimerTaskMgr.class) {
    		Timer timer = new Timer();
    		timeTaskMap.put(roomID, timer);
    	}
    }
    
    public void cancelTimerTask(int roomID) {
    	synchronized (TimerTaskMgr.class) {
    		if (timeTaskMap.containsKey(roomID)) {
    			timeTaskMap.get(roomID).cancel();
			} 
    		else {
    			System.out.println("onDeleteTimerTask roomID is not exist. roomID = " + roomID);
			}
    	}
    }
    
    public void laterGrabTimeout(int roomID) {
    	laterGrabTimeout(roomID, GRAB_TIMEOUT);
    }
    public void laterGrabTimeout(int roomID, int delay) {
    	synchronized (TimerTaskMgr.class) {
    		if (timeTaskMap.containsKey(roomID)) {
				Timer timer = timeTaskMap.get(roomID);
				timer.cancel();
				timer = new Timer();
				timer.schedule(new GrabTask(roomID), delay);
				timeTaskMap.put(roomID, timer);
			} 
    		else {
    			System.out.println("onCreateGrabTask roomID is not exist. roomID = " + roomID);
			}
    	}
    }
    
    public void laterBetTimeout(int roomID) {
    	if(GameRoomMgr.getInstance().isDemoRoom(roomID) 
				&& !GameBetMgr.getInstance().isUserBetted(roomID, GameRoomMgr.ROBOT1)){
    		int dealerId = GameGrabMgr.getInstance().getDealerId(roomID);
    		if(dealerId != GameRoomMgr.ROBOT1){
    			int bet1 = GameBetMgr.getInstance().randomBet();
    			new GameBetHandler(roomID, GameRoomMgr.ROBOT1, bet1).onResponseDataHandle();
    		}
    		if(dealerId != GameRoomMgr.ROBOT2){
    			int bet2 = GameBetMgr.getInstance().randomBet();
    			new GameBetHandler(roomID, GameRoomMgr.ROBOT2, bet2).onResponseDataHandle();
    		}
		}
    	synchronized (TimerTaskMgr.class) {
    		if (timeTaskMap.containsKey(roomID)) {
				Timer timer = timeTaskMap.get(roomID);
				timer.cancel();
				timer = new Timer();
				timer.schedule(new BetTask(roomID), BET_TIMEOUT);
				timeTaskMap.put(roomID, timer);
			} 
    		else {
    			System.out.println("onCreateBetTask roomID is not exist. roomID = " + roomID);
			}
    	}
    }
    
    public void laterAutoShowHand(int roomID) {
    	synchronized (TimerTaskMgr.class) {
    		if (timeTaskMap.containsKey(roomID)) {
				Timer timer = timeTaskMap.get(roomID);
				timer.cancel();
				timer = new Timer();
				timer.schedule(new ShowHandTask(roomID), SHOWHAND_TIMEOUT);
				timeTaskMap.put(roomID, timer);
			} 
    		else {
    			System.out.println("onCreateShowHandTask roomID is not exist. roomID = " + roomID);
			}
    	}
    }
    
    public void laterHandleGameOver(SingleOverHandler handler) {
    	synchronized (TimerTaskMgr.class) {
    		int roomID = handler.getRoomId();
    		if (timeTaskMap.containsKey(roomID)) {
				Timer timer = timeTaskMap.get(roomID);
				timer.cancel();
				timer = new Timer();
				timer.schedule(new SingleOverTask(handler), SHOWHAND_DELAY);
				timeTaskMap.put(roomID, timer);
			} 
    		else {
    			System.out.println("onCreateGameOverTask roomID is not exist. roomID = " + roomID);
			}
    	}
    }
    
    public void laterAutoDismissFail(int roomID) {
    	synchronized (TimerTaskMgr.class) {
    		if (timeTaskMap.containsKey(roomID)) {
				Timer timer = timeTaskMap.get(roomID);
				timer.cancel();
				timer = new Timer();
				timer.schedule(new DismissTask(roomID), DISMISS_VOTE_TIMEOUT);
				timeTaskMap.put(roomID, timer);
			} 
    		else {
    			System.out.println("onCreateDismissTask roomID is not exist. roomID = " + roomID);
			}
    	}
    }
}

class GrabTask extends TimerTask {
	
	private int roomID;
	
	public GrabTask(int roomID) {
		this.roomID = roomID;
	}
	
	@Override
	public void run() {
		System.out.println("Room("+this.roomID+") grab timeout, not grab user will consider as grab 1.");
		new GrabDealerHandler(this.roomID).broadcastDealer();
	}
}

class BetTask extends TimerTask {
	
	private int roomID;
	
	public BetTask(int roomID) {
		this.roomID = roomID;
	}

	@Override
	public void run() {
		System.out.println("Room("+this.roomID+") bet timeout, not bet user will consider as bet 0.");
		new GameBetHandler(roomID).afterAllBet();
	}
}

class ShowHandTask extends TimerTask {

	private int roomID;
	
	public ShowHandTask(int roomID) {
		this.roomID = roomID;
	}
	
	@Override
	public void run() {
		System.out.println("Room("+this.roomID+") show hand timeout, not show hand user will auto calculate best.");
		new GameShowHandHandler(this.roomID).afterAllShowHand();
	}
}

class SingleOverTask extends TimerTask {
	
	private SingleOverHandler handler;
	
	public SingleOverTask(SingleOverHandler handler) {
		this.handler = handler;
	}
	
	@Override
	public void run() {
		handler.onGameOverNextHandle();
	}
}

class DismissTask extends TimerTask {
	
	private int roomID;
	
	public DismissTask(int roomID) {
		this.roomID = roomID;
	}
	
	@Override
	public void run() {
		System.out.println("Room("+this.roomID+") dismiss time out, will check if dismiss according to agree count.");
		// 超时视为不同意
		new DismissRoomPollingHandler(this.roomID).afterAllDissmissVote();
	}
}
