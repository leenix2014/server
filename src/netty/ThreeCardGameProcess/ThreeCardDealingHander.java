package netty.ThreeCardGameProcess;

import netty.GameModels.GameRoomMgr;
import netty.GameModels.TimerTaskMgr;
import netty.base.DealingHander;
import protocols.config;

public class ThreeCardDealingHander extends DealingHander {

	public ThreeCardDealingHander(int roomID) {
		super(roomID);
	}
	
	public void twoPhaseDealing(int phase){
		super.dealing(phase);
		if (PHASE1 == phase && GameRoomMgr.getInstance().isWatchBet(this.roomID)) {
			if (GameRoomMgr.getInstance().getRoomConfig(this.roomID).dealer == config.bull.DEALER.GRAB) {
				TimerTaskMgr.getInstance().laterGrabTimeout(this.roomID);
			}
			else {
				TimerTaskMgr.getInstance().laterBetTimeout(this.roomID);
			}
		}
		if (PHASE2 == phase) {
			TimerTaskMgr.getInstance().laterAutoShowHand(this.roomID);
		}
	}
}
