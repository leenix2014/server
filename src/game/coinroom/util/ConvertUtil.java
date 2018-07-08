package game.coinroom.util;

import com.mozat.morange.dbcache.tables.TUsers;

import game.user.Users;
import netty.util.MathUtil;
import protocols.coinroom.common.user_info;

public class ConvertUtil {
	
	public static user_info toUserInfo(int userId){
		TUsers user = Users.load(userId);
		return toUserInfo(user);
	}

	public static user_info toUserInfo(TUsers user){
		user_info.Builder userInfo = user_info.newBuilder();
		userInfo.setAvatar(user.AVATAR_ID);
		userInfo.setCoin(user.COIN_COUNT);
		userInfo.setUserId(MathUtil.parseInt(user.LOGIN_ID));
		userInfo.setUserName(user.NAME);
		return userInfo.build();
	}
}
