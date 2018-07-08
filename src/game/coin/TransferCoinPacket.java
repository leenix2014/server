package game.coin;

import java.util.Date;

import com.mozat.morange.dbcache.tables.CoinBill;
import com.mozat.morange.dbcache.tables.TUsers;
import com.mozat.morange.dbcache.tables.Transfer;

import game.common.CommonConfig;
import game.packet.Packet;
import game.packet.PacketManager;
import game.packet.PacketTypes;
import protocols.header;
import protocols.header.packet.Builder;
import protocols.coin.transfercoin;
import protocols.coin.transfercoin.transfer_notify;

public class TransferCoinPacket extends Packet{
	//请求
	private int toId;
	private int coins;
	
	//下发
	private int error;
	private String errDesc = "";
	private int nowCoin;
	
	@Override
	public void execPacket() {
		TUsers user = TUsers.getOne(userId+"");
		if(user == null){
			error = 99;
			errDesc = "你已离线，请重新登录";
			PacketManager.send(userId, this);
			return;
		}
		if(toId == userId){
			error = 0;
			errDesc = "不能转账给自己";
			nowCoin = user.COIN_COUNT;
			PacketManager.send(userId, this);
			return;
		}
		if(coins <= 0 || coins % 100 != 0){
			error = 1;
			errDesc = "金币额为0或者不是100的整数倍";
			PacketManager.send(userId, this);
			logger.info("User({}) transfer coin failed because count({}) not valid", userId, coins);
			return;
		}
		if(user.COIN_COUNT < coins){
			error = 2;
			errDesc = "金币不足";
			PacketManager.send(userId, this);
			logger.info("User({}) transfer coin({}) failed because not enough coin({})", userId, coins, user.COIN_COUNT);
			return;
		}
		TUsers toUser = TUsers.getOne(toId+"");
		if(toUser == null){
			error = 3;
			errDesc = "转入用户不存在";
			PacketManager.send(userId, this);
			logger.info("User({}) transfer coin({}) failed because to user({}) not exists.", userId, coins, user.COIN_COUNT, toId);
			return;
		}
		int oldFrom = user.COIN_COUNT;
		nowCoin = oldFrom - coins;
		user.COIN_COUNT = nowCoin;
		
		int cost = Math.round((float)coins/100 * CommonConfig.getPercent(CommonConfig.TRANSFER_COIN_PERCENT, 3));
		
		int oldTo = toUser.COIN_COUNT;
		int nowTo = oldTo + (coins - cost);
		toUser.COIN_COUNT = nowTo;
		if(!user.update() || !toUser.update()){
			error = 4;
			errDesc = "转账失败";
			PacketManager.send(userId, this);
			logger.error("User({}) transfer coin({}) to user({}) failed because update failed. oldFrom={}, oldTo={}", userId, coins, toId, oldFrom, oldTo);
			user.COIN_COUNT = oldFrom;toUser.COIN_COUNT = oldTo;
			user.update();toUser.update();
			return;
		}
		
		Date now = new Date();
		Transfer.create(Transfer.AttrFROM_ID.set(userId+""),
				Transfer.AttrFROM_NAME.set(user.NAME),
				Transfer.AttrAMOUNT.set(coins),
				Transfer.AttrFEE.set(cost),
				Transfer.AttrTO_ID.set(toId+""),
				Transfer.AttrTO_NAME.set(toUser.NAME),
				Transfer.AttrRECORD_TIME.set(now));
		CoinBill.create(CoinBill.AttrUSER_ID.set(userId+""),
				CoinBill.AttrUSER_NAME.set(user.NAME),
				CoinBill.AttrSOURCE.set("transfercoin"),
				CoinBill.AttrSOURCE_ID.set(toId+""),
				CoinBill.AttrSOURCE_NAME.set("金币转账给"+toUser.NAME+"("+toId+")"),
				CoinBill.AttrAMOUNT.set(-coins),
				CoinBill.AttrBEFORE_BAL.set(oldFrom),
				CoinBill.AttrAFTER_BAL.set(nowCoin),
				CoinBill.AttrCREATE_TIME.set(now));
		CoinBill.create(CoinBill.AttrUSER_ID.set(toId+""),
				CoinBill.AttrUSER_NAME.set(toUser.NAME),
				CoinBill.AttrSOURCE.set("transfercoin"),
				CoinBill.AttrSOURCE_ID.set(userId+""),
				CoinBill.AttrSOURCE_NAME.set(user.NAME+"("+userId+")"+"转金币给我"),
				CoinBill.AttrAMOUNT.set(coins-cost),
				CoinBill.AttrBEFORE_BAL.set(oldTo),
				CoinBill.AttrAFTER_BAL.set(nowTo),
				CoinBill.AttrCREATE_TIME.set(now));
		error = 0;
		PacketManager.send(userId, this);
		
		
		header.packet.Builder head = header.packet.newBuilder();
    	head.setCommand(PacketTypes.TRANSFER_COIN_NOTIFY);
    	head.setVersion(1);
    	head.setSubversion(0);
    	transfer_notify.Builder notify = transfer_notify.newBuilder();
    	notify.setFromUserId(userId+"");
    	notify.setFromUserName(user.NAME);
    	notify.setAmount(coins);
    	notify.setMycoin(nowTo);
    	head.setBody(notify.buildPartial().toByteString());
    	
    	PacketManager.send(toId, head.buildPartial().toByteArray());
    	
//		MyCoinPacket coinNotify = new MyCoinPacket();
//		coinNotify.error = 0;
//		coinNotify.coinCount = nowTo;
//		PacketManager.send(toId, coinNotify);
	}

	@Override
	public void readBody(byte[] bytes) throws Exception{
		transfercoin.request request = transfercoin.request.parseFrom(bytes);
		toId = request.getToUserId();
		coins = request.getCoins();
		logger.info("User({}) request transfer coin({}) to user({})", userId, coins, toId);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		transfercoin.response.Builder builder = transfercoin.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		builder.setCoins(nowCoin);
		
    	pktBuilder.setBody(builder.buildPartial().toByteString());		
	}
}
