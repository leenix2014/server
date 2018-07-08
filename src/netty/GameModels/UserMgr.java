package netty.GameModels;

import com.mozat.morange.dbcache.tables.TUsers;

import game.packet.PacketManager;
import game.user.Users;
import game.user.packet.RoomCardPacket;

public class UserMgr {
	public static final int sidTypeToken = 0;
	public static final int sidTypeVisitor = 1;
	public static final int sidTypeFB = 2;
	public static final int sidTypePhone = 3;
	
	volatile private static UserMgr instance = null;

    private UserMgr(){
    }

    public static UserMgr getInstance() {

        if(instance == null){
            synchronized (UserMgr.class) {
                if(instance == null){
                    instance = new UserMgr();
                }
            }
        }

        return instance;
    }
    
    public String getRandomUserName(){
    	String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String name = "" + chars.charAt((int)(Math.random() * 26));
		
		for (int i = 0; i < 4; i++) {
			String chars2 = "abcdefghijklmnopqrstuvwxyz";
			name += chars2.charAt((int)(Math.random() * 26));
		}

		return name;
    }
    
    public int getCuber(int userId){
    	TUsers users = Users.load(userId);
    	if (users == null) {
			return 0;
		}
    	
    	return users.ROOM_CARD_COUNT;
    }
    
    public int getUserCoin(int userId){
    	TUsers users = Users.load(userId);
    	if (users == null) {
			return 0;
		}
    	
    	return users.COIN_COUNT;
    }
    
    public void setCuber(int userId, int cuber){
    	TUsers users = Users.load(userId);
    	if (users == null) {
			return;
		}
    	
    	users.ROOM_CARD_COUNT = cuber;
    	users.update();
    }
    
    public void setUserCoin(int userId, int count){
    	TUsers users = Users.load(userId);
    	if (users == null) {
			return;
		}
    	
    	users.COIN_COUNT = count;
    	users.update();
    }
    
    //type 更新类型 1=正常更新 2=充值添加房卡更新
    public void sendCuber(int userId, int type){
    	int roomCardCount = UserMgr.getInstance().getCuber(userId);
		RoomCardPacket roomCardPacket = new RoomCardPacket();
		roomCardPacket.type = type;
		roomCardPacket.roomCardCount = roomCardCount;				
		PacketManager.send(userId, roomCardPacket);	
    }
    
    public String getUserName(int userId){
    	TUsers user = Users.load(userId);
    	if(user == null){
    		return "";
    	}
    	return user.NAME;
    }
}


	