package game.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.game.Global;
import com.mozat.morange.log.TraceLog;
import com.mozat.morange.service.BusinessService;
import com.mozat.morange.service.ExecuteManager;
import com.mozat.morange.service.JettyService;
import com.mozat.morange.service.MonetService2;

import game.packet.PacketManager;

public class Server { 
	private static Logger log = LoggerFactory.getLogger(Server.class);
	private static Connector connector;
	private static JettyService jetty;
	private static MonetService2 monetService;
	
	public static MonetService2 getMonetService(){
		return monetService;
	}
	
	public static void main(String[] args) {
		//初始化网络包
		PacketManager.init();
		
		//初始化数据库
		try {
			Global.initMSSQLDBConn();
		} catch (Exception e1) {
			log.error("Global.init failed, ", e1);
		}
		
		//初始化业务
		BusinessService.init();
		
		//初始化jetty
		jetty = new JettyService();
		try {
			jetty.start();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			TraceLog.info("Server.main start jetty failed");
		}
		
		//初始化线程池
		ExecuteManager.init();
		
		 //新开一个线程，启动网络层
		 new Thread(new Runnable() {			
			@Override
			public void run() {
				connector = new Connector();
				connector.init();
				try {
					connector.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					connector.stop();
					log.error("Start Connector failed, ", e);
				}
			}
		}).start();
		 
		 //启动业务层
		 monetService = new MonetService2();
		 try {
			monetService.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			monetService.stop();
			log.error("Start MonetService failed, ", e);
		}
		 
		 //关闭时的处理
		 Runtime.getRuntime().addShutdownHook(new Thread(){
	        	@Override
				public void run(){
	        		Global.shutdownSQLServerCacheManager();
	        		ExecuteManager.stop();
	        		connector.stop();
	        		monetService.stop();
	        	}
	        });
	 }  
}
