package netty.BullGameProcess;

import netty.GameModels.GameRoomMgr;
import netty.GameModels.TimerTaskMgr;
import netty.base.DealingHander;
import protocols.config;

public class BullDealingHander extends DealingHander{
	
	public BullDealingHander(int roomID){
		super(roomID);
	}
	
	public void oneTimeDealing(){
		dealing(PHASE_ALL);
	}
	
	public void twoPhaseDealing(int phase) {
		dealing(phase);
		if (GameRoomMgr.getInstance().getRoomConfig(this.roomID).dealer == config.bull.DEALER.GRAB) {
			TimerTaskMgr.getInstance().laterGrabTimeout(this.roomID);
		}
		else {
			TimerTaskMgr.getInstance().laterBetTimeout(this.roomID);
		}
	}
}
