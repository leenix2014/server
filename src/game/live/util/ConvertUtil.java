package game.live.util;

import com.mozat.morange.dbcache.tables.LiveRoom;

import game.live.model.LiveRoomModel;
import game.live.model.Tiger;
import protocols.live.bettiger;
import protocols.live.common.live_room;

public class ConvertUtil {
	
	public static live_room toRoomInfo(LiveRoomModel model, int userId){
		LiveRoom room = model.getConfig();
		live_room.Builder bld = live_room.newBuilder();
		bld.setAnchor(room.ANCHOR);
		bld.setAnchorName(room.ANCHOR_NAME);
		bld.setChannel(room.CHANNEL);
		bld.setCost(room.COST);
		bld.setOnlineCount(model.getOnlineCount());
		bld.setMsgCount(model.getMsgCount());
		bld.setTitle(room.TITLE);
		bld.setType(room.ANCHOR_TYPE);
		bld.setEncrypted(room.ENCRYPTED);
		bld.setHistoryCube(model.getAnchor().HISTORY_CUBE);
		bld.setTiger(model.getTiger(userId).toClient());
		return bld.build();
	}
	
	public static bettiger.P toPoint(Tiger.P p){
		bettiger.P.Builder bld = bettiger.P.newBuilder();
		bld.setX(p.x);
		bld.setY(p.y);
		return bld.build();
	}
}
