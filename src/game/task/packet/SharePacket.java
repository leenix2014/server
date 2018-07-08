package game.task.packet;


import game.packet.Packet;
import game.packet.PacketManager;
import game.task.TaskService;
import protocols.header.packet.Builder;
import protocols.task.share;

public class SharePacket extends Packet{
	//请求
	share.request request;
	
	//下发
	public int error;

	@Override
	public void execPacket(){
		TaskService.addTaskDoneCount(userId, TaskService.TASK_TYPE_SHARE, 1);
		
		SharePacket packet = new SharePacket();
		packet.error = 0;
		PacketManager.send(userId, packet);
		logger.info("User({}) finish share task success", userId);
	}
	
	@Override
	public void readBody(byte[] bytes) throws Exception{
		request = share.request.parseFrom(bytes);
	}

	@Override
	public void writeBody(Builder pktBuilder) {	
		share.response.Builder builder = share.response.newBuilder();
		builder.setError(this.error);
		builder.setErrDesc(errDesc);
		pktBuilder.setBody(builder.buildPartial().toByteString());					
	}
}
