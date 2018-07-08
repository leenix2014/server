package game.baccarat.model;

/**
 * 路单大路、大眼仔、小路、蟑螂路等会发生庄闲切换和有长龙的路
 * @author leen
 *
 */
public class BentRoad implements Cloneable{
	
	//路单大路
	private int[][] road;
	private int x=0;//路单横轴坐标，可无限延伸
	private int y=0;//路单纵轴坐标，0-5
	private int toggleX=-1;//长龙时，x为长龙坐标，toggleX为长龙开始时坐标
	private boolean toggled = false;
	private String currWinner;
	private boolean first = true;
	
	public BentRoad(){
		road = new int[120][6];
		x=0;y=0;toggleX=-1;currWinner=null;first=true;
	}
	
	public BentRoad accept(int pivot){
		if(pivot == 0){
			return this;
		}
		//处理路单
		if(first){
			//路单刚开始
			road[x][y] = pivot;
			setWinner(pivot);
			first = false;
		} else {
			String preWinner = getWinner();
			setWinner(pivot);
			String currWinner = getWinner();
			toggled = preWinner!=null && currWinner!=null && !currWinner.equals(preWinner);
			if(!toggled){//没发生庄闲切换
				if(y==5 || road[x][y+1] != 0){//长龙
					if(toggleX == -1){
						toggleX = x;
					}
					x++;//长龙x
				} else {
					y++;
				}
			} else {
				if(toggleX != -1){
					x=toggleX+1;
					toggleX=-1;
				} else {
					x++;
				}
				y=0;
				while(road[x][y] != 0){//已有其他长龙
					x++;
				}
			}
			road[x][y] = pivot;
		}
		return this;
	}
	
	private void setWinner(int pivot){
		if(Waybill.isBankerWin(pivot)){
			currWinner = "banker";
		}
		if(Waybill.isPlayerWin(pivot)){
			currWinner = "player";
		}
	}
	
	private String getWinner(){
		return currWinner;
	}
	
	public int[][] getRoad(){
		return road;
	}
	
	@Override
	public BentRoad clone() {
		BentRoad copy = new BentRoad();
		try {
			copy =  (BentRoad) super.clone();
			copy.road = new int[120][6];
			for(int i=0;i<road.length;i++){
				for(int j=0;j<road[i].length;j++){
					copy.road[i][j] = road[i][j];
				}
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return copy;
	}
}
