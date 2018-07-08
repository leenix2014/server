package game.user.packet;

import com.google.protobuf.InvalidProtocolBufferException;
import com.mozat.morange.dbcache.tables.TUsers;

import game.common.CommonConfig;
import game.user.Users;
import protocols.header.packet.Builder;
import protocols.user.signin;

public class SignInPacket extends LoginBasePacket{
	//请求
	signin.request request;
	
	//下发
	public boolean signedup = false;
	public int playing = 0;
	
	public int uid = 0;
	public String name = "";
	public int gender = 0;
	public int diamonds = 0;
	public int chips = 0;
	public int avatarId = 0;
	public String latestVer = "";
	public boolean mustUpdate = false;
	
	@Override
	public void execPacket(){
		super.login();
	}

	@Override
	public void readBody(byte[] bytes) throws InvalidProtocolBufferException {
		request = signin.request.parseFrom(bytes);
		this.sid = request.getSid();
		this.uuid = request.getUuid();
		this.token = request.getToken();
		this.countryCode = request.getCountryCode();
		this.extra = request.getExtra();
		this.platform = request.getPlatform()==1?PLATFORM_IOS:PLATFORM_ANDROID;
		this.appVer = request.getAppVer();
		this.lang = request.getLanguage();
	}

	@Override
	public void writeBody(Builder pktBuilder) {
		signin.response.Builder sign = signin.response.newBuilder();
		sign.setError(error);
		sign.setErrDesc(errDesc);
		
		if(error == 0){
			signin.user_t.Builder user = signin.user_t.newBuilder();
	    	user.setUid(uid);
	    	user.setSid(sid);
	    	user.setToken(token);
	    	user.setName(name);
	    	user.setGender(gender);
	    	user.setDiamonds(diamonds);
	    	user.setChips(chips);
	    	user.setAvatarId(avatarId);
	    	TUsers users = Users.load(uid);
	    	if(users != null){
	    		user.setCuber(users.ROOM_CARD_COUNT);
	        	user.setCoin(users.COIN_COUNT);
	    		user.setAgentId(users.AGENT_ID);
	    		user.setCountryCode(users.COUNTRY_CODE);
	    		user.setPhoneNum(users.PHONE);
	    		user.setLang(users.LANG);
	    	}
	    
	    	sign.setUser(user);
	    	sign.setSignedup(signedup);	
	    	sign.setPlaying(playing);
	    	sign.addSwitch("on".equals(CommonConfig.get(CommonConfig.TOPUP_SWITCH))?"on":"off");
	    	sign.addSwitch("on".equals(CommonConfig.get(CommonConfig.TOPUP_CONTACT_SWITCH))?"on":"off");
	    	sign.addSwitch("on".equals(CommonConfig.get(CommonConfig.PRIZE_SWITCH))?"on":"off");
	    	sign.addSwitch("on".equals(CommonConfig.get(CommonConfig.GHOST_SWITCH))?"on":"off");
	    	sign.addSwitch("on".equals(CommonConfig.get(CommonConfig.LIVE_SWITCH))?"on":"off");
	    	sign.addSwitch("on".equals(CommonConfig.get(CommonConfig.ROULETTE_SWITCH))?"on":"off");
	    	sign.addSwitch("on".equals(CommonConfig.get(CommonConfig.COIN_ROOM_SWITCH))?"on":"off");
	    	sign.addSwitch("on".equals(CommonConfig.get(CommonConfig.BACCARAT_SWITCH))?"on":"off");
	    	sign.setLatestVer(latestVer);
	    	sign.setMustUpdate(mustUpdate);
	    	sign.setCubeCoinRate(CommonConfig.getInt(CommonConfig.CUBE_COIN_RATE, 10));
	    	sign.setTransferCoinPercent(CommonConfig.getPercent(CommonConfig.TRANSFER_COIN_PERCENT, 3));
	    	sign.setAppUrl(appUrl);
		}
		pktBuilder.setBody(sign.buildPartial().toByteString());
	}
}
