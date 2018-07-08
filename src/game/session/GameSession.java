package game.session;

import io.netty.channel.Channel;

/**
 * 
 * @author gaopeidian
 * 网络会话
 */
public class GameSession {
	private Channel channel = null;
	private int userId = 0;
	private String lang = "zh_CN";
	
	public boolean isActive(){
		if (channel == null) {
			return false;
		}
		return channel.isActive();
	}
	
	public void setChannel(Channel channel){
		this.channel = channel;
	}
	
	public Channel getChannel(){
		return this.channel;
	}
	
	public void setUserId(int userId){
		this.userId = userId;
	}
	
	public int getUserId(){
		return this.userId;
	}
	
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
}

