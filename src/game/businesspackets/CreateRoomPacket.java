package game.businesspackets;

import java.util.Set;

import game.packet.Packet;
import game.packet.PacketManager;
import game.packet.PacketTypes;
import game.task.TaskService;
import netty.GameModels.GameRoomMgr;
import netty.GameModels.GameRoomMgr.RoomConfig;
import netty.GameModels.UserMgr;
import protocols.config.bull;
import protocols.header;
import protocols.header.packet.Builder;
import protocols.game.newgame;

public class CreateRoomPacket extends Packet{
	//请求
	private int gameType;
	private bull gameConfig;
	private int lastInst;
	
	//响应
	private int error;
	private int gamingRoom;
	private int roomId;
	
	@Override
	public void readBody(byte[] bytes) throws Exception {
		newgame.request requestMsg = newgame.request.parseFrom(bytes);
		this.gameType = requestMsg.getGame();
		this.lastInst = requestMsg.getLastInst();
		this.gameConfig = requestMsg.getBull();
		logger.info("User({}) request create room, config:{}", userId, requestMsg.toString());
	}

	@Override
	public void writeBody(Builder pktBuilder) {
		newgame.response.Builder builder = newgame.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		if(error == 3){
			builder.setInst(gamingRoom);
		}
		if(error == 0){
			builder.setGame(gameType);
			builder.setInst(roomId);
			builder.setOwner(GameRoomMgr.getInstance().getOwnerSeat(roomId));
			builder.setBull(this.gameConfig);
		}

		pktBuilder.setBody(builder.buildPartial().toByteString());
	}

	@Override
	public void execPacket() throws Exception {
		if(this.gameConfig.getCoinroom()){
    		int coin = UserMgr.getInstance().getUserCoin(userId);
    		if(coin <= 0){
    			error = 4;
    			errDesc = "金币数为0, 创建房间失败";
		    	PacketManager.send(userId, this);
		    	logger.info("User({}) create room failed because no enough coin!", userId);
		    	return;
    		}
    	} else {
	    	//先判断房卡是否足够
	    	int innings = this.gameConfig.getInnings();
			int roomCardCount = UserMgr.getInstance().getCuber(userId); 
			if(innings < 1){
				innings = 1;
			}
			if (roomCardCount < innings){
				error = 1;
				errDesc = "Cuber不足, 创建房间失败";
		    	PacketManager.send(userId, this);
		    	logger.info("User({}) create room failed because no enough cuber!", userId);
		    	return;
			}
    	}
		//判断是否正在游戏中
    	gamingRoom = GameRoomMgr.getInstance().getRoomId(userId);
		if(gamingRoom != -1){
			error = 3;
			errDesc = "创建房间失败，房间"+gamingRoom+"正在进行牌局";
	    	PacketManager.send(userId, this);
	    	logger.info("User({}) create room failed because gaming in room({})!", userId, gamingRoom);
	    	return;
		}
		RoomConfig gameCfg = new RoomConfig();
		gameCfg.game = this.gameType;
		gameCfg.dealer = this.gameConfig.getDealer();
		gameCfg.bscores = this.gameConfig.getBscores()<0?1:this.gameConfig.getBscores();
		gameCfg.seats = this.gameConfig.getSeats()<0?5:this.gameConfig.getSeats();
		gameCfg.innings = this.gameConfig.getInnings()<0?5:this.gameConfig.getInnings();
		gameCfg.pmscores = this.gameConfig.getPmscores()<0?0:this.gameConfig.getPmscores();
		gameCfg.dmscores = this.gameConfig.getDmscores()<0?0:this.gameConfig.getDmscores();
		gameCfg.blind = this.gameConfig.getBlind();
		gameCfg.maxBet = this.gameConfig.getMaxBet();
		gameCfg.hasGhost = this.gameConfig.getHasGhost();
		gameCfg.drawPercent = this.gameConfig.getDrawPercent()>20?20:this.gameConfig.getDrawPercent();
		gameCfg.coinroom = this.gameConfig.getCoinroom();
		roomId = GameRoomMgr.getInstance().generateRoom(userId, gameCfg);
		if(roomId == -1) {
			error = 2;
			errDesc = "创建房间失败，房间号已用尽";
	    	PacketManager.send(userId, this);
	    	logger.info("User({}) create room failed because no more room!", userId);
	    	return;
		}
		//添加任务计数
		TaskService.addTaskDoneCount(userId, TaskService.TASK_TYPE_CREATE_ROOM, 1);
		
		error = 0;
    	PacketManager.send(userId, this);
    	logger.info("User({}) create room({}) success!", userId, roomId);
    	
    	if(lastInst != 0){
    		//再来一次通知房间内其他用户
    		newgame.againNotify.Builder againBuilder = newgame.againNotify.newBuilder();
    		againBuilder.setInst(roomId);
    		againBuilder.setOwnerName(UserMgr.getInstance().getUserName(userId));
    		header.packet.Builder head = header.packet.newBuilder();
    		head.setCommand(PacketTypes.AGAIN_NOTIFY_CMD);
        	head.setVersion(1);
        	head.setSubversion(0);
    		head.setBody(againBuilder.buildPartial().toByteString());
    		byte[] msgContent = head.buildPartial().toByteArray();
    		Set<Integer> uids = GameRoomMgr.getInstance().getDismissRoomUsers(lastInst);
    		for(Integer uid : uids){
    			if(uid == this.userId){
    				continue;
    			}
    			PacketManager.send(uid, msgContent);
    		}
    	}
	}
}
