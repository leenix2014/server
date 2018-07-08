package game.session;

import java.util.concurrent.ConcurrentHashMap;

import game.baccarat.model.BaccaratRoomService;
import game.roulette.model.RouletteRoomService;
import netty.GameCommon.OfflineHandler;

/**
 * 
 * @author gaopeidian
 * 会话管理
 */
public class SessionManager {
	//<userId,session>
	private static ConcurrentHashMap<Integer, GameSession> sessionMap = new ConcurrentHashMap<Integer, GameSession>();
	
	public static GameSession getSession(Integer userId){
		return sessionMap.get(userId);
	}
	
	public static GameSession kickSession(Integer userId){
		new OfflineHandler(userId).processHandle();
		return sessionMap.remove(userId);
	}
	
	public static void addSession(String lang, Integer userId, GameSession session){
		session.setLang(lang);
		session.setUserId(userId);
		sessionMap.put(userId, session);
		RouletteRoomService.onlineAgain(userId);
		BaccaratRoomService.onlineAgain(userId);
	}
	
	public static boolean isOffline(Integer userId){
		return !sessionMap.containsKey(userId);
	}
}

