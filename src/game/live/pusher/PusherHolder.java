package game.live.pusher;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PusherHolder {
	
    volatile private static PusherHolder instance = null;
    private PusherHolder(){
    }

    public static PusherHolder getInstance() {

        if(instance == null){
            synchronized (PusherHolder.class) {
                if(instance == null){
                    instance = new PusherHolder();
                }
            }
        }

        return instance;
    }
    private static class Entry {
    	private int userId;
    	private Date time;
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		public Date getTime() {
			return time;
		}
		public void setTime(Date time) {
			this.time = time;
		}
    }
    // Map<roomId, Map<userId, time>
    private Map<Integer, Entry> pusherHolders = new HashMap<>();
    
    public void addHolder(int roomId, int userId){
    	synchronized (PusherHolder.class) {
    		Entry holder = new Entry();
    		holder.setUserId(userId);
    		holder.setTime(new Date());
    		pusherHolders.put(roomId, holder);
    	}
    }
    
    public int getHolder(int roomId){
    	synchronized (PusherHolder.class) {
    		Entry holder = pusherHolders.get(roomId);
    		if(holder == null){
    			return 0;
    		}
    		Date now = new Date();
    		long diff = (now.getTime() - holder.getTime().getTime())/1000;
    		if(diff > 5){
    			return 0;
    		}
    		return holder.getUserId();
    	}
    }
}
