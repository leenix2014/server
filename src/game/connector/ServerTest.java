package game.connector;

public class ServerTest { 
	 public static void main(String[] args) {
		 new Thread(new Runnable() {			
			@Override
			public void run() {
				Connector connector = new Connector();
				connector.init();
				try {
					connector.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	 }  
}
