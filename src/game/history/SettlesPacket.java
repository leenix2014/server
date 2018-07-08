package game.history;

import java.util.List;

import com.mozat.morange.dbcache.tables.TRoomConfig;
import com.mozat.morange.dbcache.tables.TRoomInnings;

import game.packet.PacketManager;
import netty.GameEvaluation.BullCardsConstantInterface;
import protocols.header.packet.Builder;
import protocols.history.settles;

public class SettlesPacket extends HistoryBasePacket{
	//请求
	private int roomId;
	
	//下发
	private TRoomConfig roomConfig;
	private List<TRoomInnings> innings;
	
	@Override
	public void execPacket(){
		List<TRoomConfig> roomConfigs = TRoomConfig.getManyBySQL("SELECT c.* FROM ROOM_CONFIG c, ROOM_INNINGS t WHERE c.ROOM_ID = ? AND c.ROOM_ID = t.ROOM_ID AND t.USER_ID = ? AND t.INNING = 0", this.roomId, this.userId);
		if(roomConfigs != null && roomConfigs.size() > 0) {
			roomConfig = roomConfigs.get(0);
			innings = TRoomInnings.getManyByCriteria(TRoomInnings.AttrROOM_ID.eq(roomConfig.ROOM_ID),
					TRoomInnings.AttrINNING.ne(0));
		}
		PacketManager.send(userId, this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		settles.settleRequest request = settles.settleRequest.parseFrom(bytes);
		roomId = request.getRoomId();
		logger.info("User({}) request get room({}) innings", userId, roomId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {
		settles.settleResponse.Builder builder = settles.settleResponse.newBuilder();
		if(roomConfig == null){
			builder.setError(1);
			builder.setErrDesc("房间号不存在");
		} else {
			builder.setError(0);
			builder.setRoomConfig(toBuilder(roomConfig));
			
			boolean threeFiveCard = BullCardsConstantInterface.ThreeCardGame == roomConfig.GAME;
			if(innings != null){
				for(TRoomInnings inning : innings){
					if(threeFiveCard){
						builder.addSettles(toBuilder(inning, true));//三张牌局
						builder.addSettles(toBuilder(inning, false));//五张牌局
					} else {
						builder.addSettles(toBuilder(inning, false));//五张牌局
					}
				}
			}
		}
		
		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
