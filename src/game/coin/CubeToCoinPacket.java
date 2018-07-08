package game.coin;

import java.util.Date;

import com.mozat.morange.dbcache.tables.CoinBill;
import com.mozat.morange.dbcache.tables.RoomCardBill;
import com.mozat.morange.dbcache.tables.TUsers;

import game.common.CommonConfig;
import game.packet.Packet;
import game.packet.PacketManager;
import netty.GameModels.UserMgr;
import protocols.header.packet.Builder;
import protocols.coin.cubetocoin;

public class CubeToCoinPacket extends Packet{
	//请求
	private int cubes;
	
	//下发
	private int error;
	private String errDesc = "";
	private int nowCube;
	private int nowCoin;
	private int successCoin=0;
	
	@Override
	public void execPacket() {
		TUsers user = TUsers.getOne(userId+"");
		if(user == null){
			error = 99;
			errDesc = "你已离线，请重新登录";
			PacketManager.send(userId, this);
			return;
		}
		if(cubes == 0){
			error = 0;
			errDesc = "转换数额为0";
			nowCube = user.ROOM_CARD_COUNT;
			nowCoin = user.COIN_COUNT;
			PacketManager.send(userId, this);
			return;
		}
		if(user.ROOM_CARD_COUNT < cubes || cubes < 0){
			error = 1;
			errDesc = "Cube不足";
			PacketManager.send(userId, this);
			logger.info("User({}) cube to coin failed because no enough cube. user cube:{}, request cube:{}", userId, user.ROOM_CARD_COUNT, cubes);
			return;
		}
		int oldCube = user.ROOM_CARD_COUNT;
		nowCube = oldCube - cubes;
		user.ROOM_CARD_COUNT = nowCube;
		
		int oldCoin = user.COIN_COUNT;
		int amount = cubes * CommonConfig.getInt(CommonConfig.CUBE_COIN_RATE, 10);
		nowCoin = oldCoin + amount;
		user.COIN_COUNT = nowCoin;
		if(!user.update()){
			error = 98;
			errDesc = "服务端更新失败，请重试";
			PacketManager.send(userId, this);
			logger.info("User({}) cube to coin failed because update failed.", userId);
			return;
		}
		successCoin = amount;
		
		Date now = new Date();
		RoomCardBill.create(RoomCardBill.AttrUSER_ID.set(userId+""), 
				RoomCardBill.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(userId)),
				RoomCardBill.AttrSOURCE.set("coin"),
				RoomCardBill.AttrSOURCE_ID.set(cubes+""),
				RoomCardBill.AttrSOURCE_NAME.set("Cube转Coin"),
				RoomCardBill.AttrAMOUNT.set(-cubes),
				RoomCardBill.AttrBEFORE_BAL.set(oldCube),
				RoomCardBill.AttrAFTER_BAL.set(nowCube),
				RoomCardBill.AttrCREATE_TIME.set(now));
		CoinBill.create(CoinBill.AttrUSER_ID.set(userId+""),
				CoinBill.AttrUSER_NAME.set(UserMgr.getInstance().getUserName(userId)),
				CoinBill.AttrSOURCE.set("cube"),
				CoinBill.AttrSOURCE_ID.set(cubes+""),
				CoinBill.AttrSOURCE_NAME.set("Cube转Coin"),
				CoinBill.AttrAMOUNT.set(amount),
				CoinBill.AttrBEFORE_BAL.set(oldCoin),
				CoinBill.AttrAFTER_BAL.set(nowCoin),
				CoinBill.AttrCREATE_TIME.set(now));
		error = 0;
		PacketManager.send(userId, this);
	}

	@Override
	public void readBody(byte[] bytes) throws Exception{
		cubetocoin.request request = cubetocoin.request.parseFrom(bytes);
		cubes = request.getCubes();
		logger.info("User({}) request cube({}) to coin.", userId, cubes);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		cubetocoin.response.Builder builder = cubetocoin.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setCubes(nowCube);
		builder.setCoins(nowCoin);
		builder.setSuccessCoin(successCoin);
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
