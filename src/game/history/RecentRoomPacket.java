package game.history;

import java.util.List;

import com.mozat.morange.dbcache.tables.TRoomConfig;

import game.packet.PacketManager;
import protocols.header.packet.Builder;
import protocols.history.rooms;

public class RecentRoomPacket extends HistoryBasePacket{
	//请求
	
	//下发
	private List<TRoomConfig> roomConfigs;
	
	@Override
	public void execPacket(){
		roomConfigs = TRoomConfig.getManyBySQL("SELECT c.* FROM ROOM_CONFIG c, ROOM_INNINGS t WHERE c.ROOM_ID = t.ROOM_ID AND t.USER_ID = ? AND t.INNING = 0 AND EXISTS(SELECT 1 FROM ROOM_INNINGS d WHERE c.ROOM_ID = d.ROOM_ID AND d.USER_ID = ? AND d.INNING = 1 ) ORDER BY c.CREATE_TIME DESC", this.userId, this.userId);
		PacketManager.send(userId, this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		logger.info("User({}) request get recent rooms", userId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {
		rooms.response.Builder builder = rooms.response.newBuilder();
		if(roomConfigs != null){
			for(TRoomConfig roomConfig : roomConfigs){
				builder.addRecentRooms(toBuilder(roomConfig));
			}
		}
		
		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
