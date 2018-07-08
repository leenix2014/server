package game.history;

import java.util.List;

import com.mozat.morange.dbcache.tables.CoinBill;
import com.mozat.morange.dbcache.tables.RoomCardBill;
import com.mozat.morange.util.DateUtil;

import game.packet.Packet;
import game.packet.PacketManager;
import protocols.header.packet.Builder;
import protocols.history.bills;

public class BillsPacket extends Packet {
	//请求
	private String lang;
	private String type;
	
	//下发
	private List<RoomCardBill> cubeBills;
	private List<CoinBill> coinBills;
	
	@Override
	public void execPacket(){
		if("coin".equals(type)){
			coinBills = CoinBill.getManyBySQL("SELECT * FROM COIN_BILL t WHERE t.`USER_ID` = ? ORDER BY t.`CREATE_TIME` DESC limit 30", this.userId);
		} else {
			cubeBills = RoomCardBill.getManyBySQL("SELECT * FROM ROOM_CARD_BILL t WHERE t.`USER_ID` = ? ORDER BY t.`CREATE_TIME` DESC limit 30", this.userId);
		}
		PacketManager.send(userId, this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		bills.request request = bills.request.parseFrom(bytes);
		lang = request.getLang();
		type = request.getType();
		logger.info("User({}) request {} bills.", userId, type);
	}

	@Override
	public void writeBody(Builder pktBuilder) {
		bills.response.Builder builder = bills.response.newBuilder();
		if(cubeBills != null){
			for(RoomCardBill myBill : cubeBills){
				builder.addBills(toBuilder(myBill));
			}
		}
		if(coinBills != null){
			for(CoinBill coinBill : coinBills){
				bills.bill_t.Builder billBuilder = bills.bill_t.newBuilder();
				billBuilder.setSource(i18n(coinBill.SOURCE));
				billBuilder.setSourceDesc(coinBill.SOURCE_NAME);
				billBuilder.setAmount(coinBill.AMOUNT);
				billBuilder.setBeforeBal(coinBill.BEFORE_BAL);
				billBuilder.setAfterBal(coinBill.AFTER_BAL);
				billBuilder.setDatetime(DateUtil.yyyyMMddHHmmss(coinBill.CREATE_TIME));
				builder.addBills(billBuilder);
			}
		}
		
		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
	
	protected bills.bill_t.Builder toBuilder(RoomCardBill bill){
		bills.bill_t.Builder billBuilder = bills.bill_t.newBuilder();
		billBuilder.setSource(i18n(bill.SOURCE));
		billBuilder.setSourceDesc(bill.SOURCE_NAME);
		billBuilder.setAmount(bill.AMOUNT);
		billBuilder.setBeforeBal(bill.BEFORE_BAL);
		billBuilder.setAfterBal(bill.AFTER_BAL);
		billBuilder.setDatetime(DateUtil.yyyyMMddHHmmss(bill.CREATE_TIME));
		return billBuilder;
	}
	
	private String i18n(String source){
		boolean english = "en_US".equals(lang);
		if("topup".equals(source)){
			return english?"Top Up":"充值";
		}
		if("transfer".equals(source)){
			return english?"Transfer":"转账";
		}
		if("signin".equals(source)){
			return english?"Login":"登录";
		}
		if("task".equals(source)){
			return english?"Task":"任务";
		}
		if("prize".equals(source)){
			return english?"Lottery":"抽奖";
		}
		if("consume".equals(source)){
			return english?"Consume":"消费";
		}
		if("appstore".equals(source)){
			return english?"App Store":"苹果商城";
		}
		if("live".equals(source)){
			return english?"Live":"直播";
		}
		if("coin".equals(source)){
			return english?"Cube to coin":"转换为金币";
		}
		// 以下金币账单
		if("withdraw".equals(source)){
			return english?"Withdraw":"金币提现";
		}
		if("cube".equals(source)){
			return english?"Cube to coin":"转换为金币";
		}
		if("coinroom".equals(source)){
			return english?"Gold Field":"金币场";
		}
		if("roulette".equals(source)){
			return english?"Roulette":"轮盘";
		}
		if("transfercoin".equals(source)){
			return english?"Transfer Coin":"金币转账";
		}
		return source;
	}
}
