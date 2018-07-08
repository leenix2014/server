package game.coin;


import com.mozat.morange.dbcache.tables.TUsers;

import game.packet.Packet;
import game.packet.PacketManager;
import protocols.header.packet.Builder;
import protocols.coin.mycoin;

public class MyCoinPacket extends Packet{
	//请求
	
	//下发
	public int coinCount;
	public int withdrawCount;
	
	@Override
	public void execPacket() {
		TUsers user = TUsers.getOne(userId+"");
		if(user == null){
			error = 1;
			errDesc = "你已离线，请重新登录";
			PacketManager.send(userId, this);
			return;
		}
		error = 0;
		coinCount = user.COIN_COUNT;
		PacketManager.send(userId, this);
	}

	@Override
	public void readBody(byte[] bytes) throws Exception{
		//mycoin.request request = mycoin.request.parseFrom(bytes);
		logger.info("User({}) query coin count.", userId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		mycoin.response.Builder builder = mycoin.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setCoins(coinCount);
		builder.setWithdrawCoin(withdrawCount);
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
