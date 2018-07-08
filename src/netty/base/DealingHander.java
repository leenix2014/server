package netty.base;

import java.util.List;
import java.util.Map;

import game.packet.PacketManager;
import game.packet.PacketTypes;
import netty.GameModels.GameRoomMgr;
import netty.GameModels.PlayerCardsMgr;
import protocols.header;
import protocols.bull.dealing;

public class DealingHander {
	
	public static int PHASE_ALL = 0;//一次性发五张牌
	
	public static int PHASE1 = 1;//先发三张牌
	
	public static int PHASE2 = 2;//再发两张牌
	
	protected int roomID;
	
	public DealingHander(int roomID){
		this.roomID = roomID;
	}
	
	protected void dealing(int phase) {
		Map<Integer, List<Integer>> handCards = null;
		if (PHASE1 == phase || PHASE_ALL == phase) {
			handCards = PlayerCardsMgr.getInstance().onPlayerDealing(this.roomID);
			if(PHASE_ALL == phase){
				notifyHandCards(handCards, 0, 5);
			} else {
				notifyHandCards(handCards, 0, 3);
			}
			PlayerCardsMgr.getInstance().setPhaseStatus(this.roomID, phase);
		}
		
		if (PHASE2 == phase) {
			handCards = PlayerCardsMgr.getInstance().getRoomHandCard(this.roomID);
			notifyHandCards(handCards, 3, 5);
			PlayerCardsMgr.getInstance().setPhaseStatus(this.roomID, DealingHander.PHASE2);
		}
	}
	
	private void notifyHandCards(Map<Integer, List<Integer>> handCards, int startIdx, int endIdx){
		if(handCards == null){
			return;
		}
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.DEALING_CMD);
    	head.setVersion(1);
    	head.setSubversion(0);
		for(Map.Entry<Integer, List<Integer>> entry : handCards.entrySet()){
			int userId = entry.getKey();
			List<Integer> cards = entry.getValue();
		    
		    dealing.response.Builder respBuilder = dealing.response.newBuilder();
		    respBuilder.setSeat(GameRoomMgr.getInstance().getSeatId(roomID, userId));
		    for (int i = startIdx; i < endIdx; ++i) {
		    	respBuilder.addHand(cards.get(i));
			}

		    head.setBody(respBuilder.buildPartial().toByteString());
			
	    	byte[] msgContent = head.buildPartial().toByteArray();
	    	PacketManager.send(userId, msgContent);
		}
	}
}
