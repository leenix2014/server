package com.mozat.morange.notification;

import com.mozat.morange.dbcache.tables.BTNoticationServer;
import com.mozat.morange.game.Global;
import com.mozat.morange.log.TraceLog;

public class NotificationDataEntity {
	
	public static void safeSaveLoginServerId(int monetId){
		try {
			if(Global.getServerId() != NotificationDataEntity.getLastLoginServerId(monetId)){
		      	   NotificationDataEntity.saveLastLoginServerId(monetId, Global.getServerId());
		         }
		}  catch (Exception ignored) {
			TraceLog.info("[NotificationDataEntity] safeSaveLoginServerId is fail   monetId = "+monetId+"  exceotion = "+ignored);
		}
		
	}
	

	public static int getLastLoginServerId(int monetId){
		BTNoticationServer s = BTNoticationServer.getOne(monetId);
		if(null == s){
			return -1;
		}
		return s.serverId;
	}
    
	public static void saveLastLoginServerId(int monetId,int serverId){
		
		BTNoticationServer s = BTNoticationServer.getOne(monetId);
		if (null == s) {
			create(monetId, serverId);
			return;
		}
		update(monetId, serverId);
	}
	
	private static BTNoticationServer create(int monetId, int serverId){
		return BTNoticationServer.create(monetId, BTNoticationServer.AttrServerId.set(serverId));
	}
	
	private static void update(int monetId, int serverId){
		BTNoticationServer.updateByCriteria(
				BTNoticationServer.valueList(
						BTNoticationServer.AttrServerId.set(serverId)
						),
				BTNoticationServer.AttrMonetId.eq(monetId));
	}
	
}
