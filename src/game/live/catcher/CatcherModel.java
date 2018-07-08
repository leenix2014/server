package game.live.catcher;

import java.util.HashMap;
import java.util.Map;

public class CatcherModel {
	
    volatile private static CatcherModel instance = null;
    private CatcherModel(){
    }

    public static CatcherModel getInstance() {

        if(instance == null){
            synchronized (CatcherModel.class) {
                if(instance == null){
                    instance = new CatcherModel();
                }
            }
        }

        return instance;
    }
    private static class Entry {
    	private int userId;
    	private int count;
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
    }
    // Map<roomId, Map<userId, count>
    private Map<Integer, Entry> catcherHolders = new HashMap<>();
    
    public void addHolder(int roomId, int userId, int count){
    	synchronized (CatcherModel.class) {
    		Entry holder = new Entry();
    		holder.setUserId(userId);
    		holder.setCount(count);
    		catcherHolders.put(roomId, holder);
    	}
    }
    
    public int getHolder(int roomId){
    	synchronized (CatcherModel.class) {
    		Entry holder = catcherHolders.get(roomId);
    		if(holder == null){
    			return 0;
    		}
    		return holder.getUserId();
    	}
    }
    
    public int getRemainCount(int roomId){
    	synchronized (CatcherModel.class) {
    		Entry holder = catcherHolders.get(roomId);
    		if(holder == null){
    			return 0;
    		}
    		return holder.getCount();
    	}
    }
    
    public int decrease(int roomId){
    	synchronized (CatcherModel.class) {
    		Entry holder = catcherHolders.get(roomId);
    		if(holder == null){
    			return -1;
    		}
    		holder.setCount(holder.getCount()-1);
    		return holder.getCount();
    	}
    }
    
    public void startCountDown(int roomId){
    	
    }
}
