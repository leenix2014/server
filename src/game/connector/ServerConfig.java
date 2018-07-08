package game.connector;

public class ServerConfig {
	public int port;
	public int threadNumber;
	public int frontendSendBuf;
	public int frontendRecvBuf;
	public int frontendTimeout;
	
	ServerConfig(){
		//this.port = 8090;
		this.port = 8003;
		//this.port = 8004;
		this.threadNumber = Runtime.getRuntime().availableProcessors() * 2;
		this.frontendSendBuf = 128 * 1024;
		this.frontendRecvBuf = 128 * 1024;
		this.frontendTimeout = 120;
	}
}
