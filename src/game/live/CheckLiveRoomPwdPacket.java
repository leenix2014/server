package game.live;

import com.mozat.morange.dbcache.tables.LiveRoom;
import com.mozat.morange.util.MD5;

import game.packet.Packet;
import game.packet.PacketManager;
import netty.util.StringUtil;
import protocols.header.packet.Builder;
import protocols.live.checkpwd;

public class CheckLiveRoomPwdPacket extends Packet{
	//请求
	private int roomId;
	private String pwd;
	
	//下发
	
	@Override
	public void execPacket(){
		LiveRoom room = LiveRoom.getOne(roomId);
		if(room == null){
			error = 1;
			errDesc = "房间号不存在";
			PacketManager.send(userId, this);
			return;
		}
		if(!StringUtil.nonNull(pwd).equals(MD5.getHashString(room.PWD))){
			error = 2;
			errDesc = "密码不正确";
			PacketManager.send(userId, this);
			return;
		}
		PacketManager.send(userId, this);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		checkpwd.request request = checkpwd.request.parseFrom(bytes);
		roomId = request.getRoomId();
		pwd = request.getPwd();
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		checkpwd.response.Builder builder = checkpwd.response.newBuilder();
		builder.setError(error);
		builder.setErrDesc(errDesc);
		
		pktBuilder.setBody(builder.buildPartial().toByteString());
	}
}
