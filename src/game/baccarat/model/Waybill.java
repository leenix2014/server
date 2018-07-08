package game.baccarat.model;

import netty.util.MathUtil;

/**
 * 路单大路:
 * 1=庄 2=闲 3=和
 * 4=庄+庄对 5=闲+庄对 6=和+庄对
 * 7=庄+闲对 8=闲+闲对 9=和+闲对
 * 10=庄+庄对+闲对 11=闲+庄对+闲对 12=和+庄对+闲对
 * 13=庄+天牌 14=闲+天牌 15=和+天牌
 * 16=庄+庄对+天牌 17=闲+庄对+天牌 18=和+庄对+天牌
 * 19=庄+闲对+天牌 20=闲+闲对+天牌 21=和+闲对+天牌
 * 22=庄+庄对+闲对+天牌 23=闲+庄对+闲对+天牌 24=和+庄对+闲对+天牌
 * 
 * 路单珠盘路:
 * 1=庄 2=闲 3=和
 * @author leen
 *
 */
public class Waybill {
	
	// 统计信息
	private int size = 0;        //总局数
	private int bankerWin = 0;   //庄赢局数
	private int playerWin = 0;   //闲赢局数
	private int tie = 0;         //和局数
	private int bankerPair = 0;  //庄对局数
	private int playerPair = 0;  //闲对局数
	private int sky = 0;     //天牌局数
	
	//路单大路
	private BentRoad mainRoad = new BentRoad();
	private StraightRoad sMainRoad = new StraightRoad();
	
	//路单珠盘路
	private int[][] dishRoad;
	
	private int[] bankerAsk;//庄问路
	private int[] playerAsk;//闲问路
	//路单大眼仔
	private BentRoad bigEyeRoad = new BentRoad();
	//路单小路
	private BentRoad smallRoad = new BentRoad();
	//路单蟑螂路
	private BentRoad roachRoad = new BentRoad();
	
	public Waybill(){
		clear();
	}
	
	public void clear(){
		size = 0;
		bankerWin = 0;
		playerWin = 0;
		tie = 0;
		bankerPair = 0;
		playerPair = 0;
		sky = 0;
		
		mainRoad = new BentRoad();
		sMainRoad = new StraightRoad();
		
		dishRoad = new int[120][6];
		bigEyeRoad = new BentRoad();
		smallRoad = new BentRoad();
		roachRoad = new BentRoad();
		bankerAsk = new int[3];
		playerAsk = new int[3];
	}
	
	public Waybill accept(int pivot, int times){
		for(int i=0;i<times;i++){
			accept(pivot);
		}
		return this;
	}
	
	public void accept(int pivot){
		mainRoad.accept(pivot);
		if(!isTie(pivot)){
			sMainRoad.accept(pivot);
			bigEyeRoad.accept(sMainRoad.getBigEye());
			smallRoad.accept(sMainRoad.getSmall());
			roachRoad.accept(sMainRoad.getRoach());
			
			bankerAsk[0] = sMainRoad.clone().accept(1).getBigEye();
			bankerAsk[1] = sMainRoad.clone().accept(1).getSmall();
			bankerAsk[2] = sMainRoad.clone().accept(1).getRoach();
			playerAsk[0] = sMainRoad.clone().accept(2).getBigEye();
			playerAsk[1] = sMainRoad.clone().accept(2).getSmall();
			playerAsk[2] = sMainRoad.clone().accept(2).getRoach();
		}
		
		//处理路单珠盘路
		int i=size/6;
		int j=size%6;
		dishRoad[i][j] = getWinTarget(pivot);
		
		//统计信息
		size++;
		if(isBankerWin(pivot)){
			bankerWin++;
		}
		if(isBankerPair(pivot)){
			bankerPair++;
		}
		if(isPlayerWin(pivot)){
			playerWin++;
		}
		if(isPlayerPair(pivot)){
			playerPair++;
		}
		if(isTie(pivot)){
			tie++;
		}
		if(isSky(pivot)){
			sky++;
		}
	}
	
	public void print(){
		int[][] road = mainRoad.getRoad();
		for(int i=0;i<road.length;i++){
			for(int j=0;j<road[i].length;j++){
				int pivot = road[i][j];
				if(isBankerWin(pivot)){
					System.out.print("red ");
				} else if(isTie(pivot)){
					System.out.print("green ");
				} else if(isPlayerWin(pivot)){
					System.out.print("blue ");
				} else {
					System.out.print("X ");
				}
			}
			System.out.println();
		}
		
		for(int i=0;i<dishRoad.length;i++){
			for(int j=0;j<dishRoad[i].length;j++){
				int target = dishRoad[i][j];
				System.out.print(target+" ");
			}
			System.out.println();
		}
	}
	
	public static int getWinTarget(int pivot){
		return (pivot-1)%3 + 1;
	}

	public static boolean isBankerWin(int pivot){
		return (pivot-1)%3 == 0;
	}
	
	public static boolean isPlayerWin(int pivot){
		return (pivot-1)%3 == 1;
	}
	
	public static boolean isTie(int pivot){
		return (pivot-1)%3 == 2;
	}
	
	public int[][] getMainRoad(){
		return mainRoad.getRoad();
	}
	
	public int[][] getDishRoad(){
		return dishRoad;
	}
	
	public int[][] getBigEyeRoad() {
		return bigEyeRoad.getRoad();
	}

	public int[][] getSmallRoad() {
		return smallRoad.getRoad();
	}

	public int[][] getRoachRoad() {
		return roachRoad.getRoad();
	}

	public int[] getBankerAsk() {
		return bankerAsk;
	}

	public int[] getPlayerAsk() {
		return playerAsk;
	}

	public String getMainRoadString(){
		return toArray2String(mainRoad.getRoad());
	}
	
	public String getDishRoadString(){
		return toArray2String(dishRoad);
	}
	
	private String toArray2String(int[][] a){
		if(a == null || a.length == 0){
			return "[]";
		}
		StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i=0;i<a.length;i++) {
        	String column = toArray1String(a[i]);
        	if(column.isEmpty()){
        		if(i==a.length-1){
        			sb.append(']').toString();
        		}
        		continue;
        	}
        	if(i>=1){
        		sb.append(",");
        	}
            sb.append(column);
        }
		return sb.toString();
	}
	
	private String toArray1String(int[] a){
		if(a == null || a.length == 0){
			return "";
		}
		if(MathUtil.isAllZero(a)){
			return "";
		}
		StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i=0;i<a.length;i++) {
            sb.append(a[i]);
            if (i==a.length-1){
                sb.append(']').toString();
            } else {
            	sb.append(',');
            }
        }
		return sb.toString();
	}
	
	public static boolean isBankerPair(int pivot){
		return ((pivot-1)/3)%2 == 1;
	}
	
	public static boolean isPlayerPair(int pivot){
		return ((pivot-1)/6)%2 == 1;
	}
	
	public static boolean isSky(int pivot){
		return ((pivot-1)/12)%2 == 1;
	}
	
	public int size(){
		return size;
	}
	
	public int getBankerWin() {
		return bankerWin;
	}

	public int getPlayerWin() {
		return playerWin;
	}

	public int getTie() {
		return tie;
	}

	public int getBankerPair() {
		return bankerPair;
	}

	public int getPlayerPair() {
		return playerPair;
	}

	public int getSky() {
		return sky;
	}

	public static void main(String[] args){
		Waybill way = new Waybill();
		way.accept(1, 7);//庄赢
		way.accept(3, 3);//和
		way.accept(2, 11);//闲赢
		way.accept(1, 11);//庄赢
		way.print();
		System.out.println(way.getMainRoadString());
		System.out.println(way.getDishRoadString());
	}
}
