package game.baccarat.model;

public class StraightRoad implements Cloneable{
	
	//路单大路
	private int[][] road;
	private int x=0;//路单横轴坐标，可无限延伸
	private int y=0;//路单纵轴坐标，0-5
	private int preX=-1;
	private int preY=-1;
	private boolean toggled = false;
	private String currWinner;
	private boolean first = true;
	
	public StraightRoad(){
		road = new int[120][60];
		x=0;y=0;currWinner=null;first=true;
	}
	
	public StraightRoad accept(int pivot, int times){
		for(int i=0;i<times;i++){
			accept(pivot);
		}
		return this;
	}
	
	public StraightRoad accept(int pivot){
		if(pivot == 0 || Waybill.isTie(pivot)){
			return this;
		}
		//处理路单
		if(first){
			//路单刚开始
			road[x][y] = pivot;
			setWinner(pivot);
			first = false;
		} else {
			preX=x;preY=y;
			String preWinner = getWinner();
			setWinner(pivot);
			String currWinner = getWinner();
			toggled = preWinner!=null && currWinner!=null && !currWinner.equals(preWinner);
			if(!toggled){//没发生庄闲切换
				y++;
			} else {
				x++;
				y=0;
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
	
	public int getBigEye(){
		if(x <= 0){
			return 0;
		}
		if(!toggled){
			//路中牌
			if(y>=1 && road[x-1][y] == 0 && road[x-1][y-1] != 0){
				return 2;//蓝色
			}
			return 1;//红色
		} else {
			//路头牌
			if(preX <= 0){
				return 0;
			}
			int ifY = preY + 1;//假设没发生toggle
			if(ifY>=1 && road[preX-1][ifY] == 0 && road[preX-1][ifY-1] != 0){
				return 1;//红色，颜色取反
			}
			return 2;//蓝色，颜色取反
		}
	}
	
	public int getSmall(){
		if(x <= 1){
			return 0;
		}
		if(!toggled){
			//路中牌
			if(y>=1 && road[x-2][y] == 0 && road[x-2][y-1] != 0){
				return 2;//蓝色
			}
			return 1;//红色
		} else {
			//路头牌
			if(preX <= 1){
				return 0;
			}
			int ifY = preY + 1;//假设没发生toggle
			if(ifY>=1 && road[preX-2][ifY] == 0 && road[preX-2][ifY-1] != 0){
				return 1;//红色
			}
			return 2;//蓝色
		}
	}
	
	public int getRoach(){
		if(x <= 2){
			return 0;
		}
		if(!toggled){
			//路中牌
			if(y>=1 && road[x-3][y] == 0 && road[x-3][y-1] != 0){
				return 2;//蓝色
			}
			return 1;//红色
		} else {
			//路头牌
			if(preX <= 2){
				return 0;
			}
			int ifY = preY + 1;//假设没发生toggle
			if(ifY>=1 && road[preX-3][ifY] == 0 && road[preX-3][ifY-1] != 0){
				return 1;//红色
			}
			return 2;//蓝色
		}
	}
	
	@Override
	public StraightRoad clone() {
		StraightRoad copy = new StraightRoad();
		try {
			copy =  (StraightRoad) super.clone();
			if(this.currWinner == null){
				copy.currWinner = null;
			} else {
				copy.currWinner = new String(this.currWinner);
			}
			copy.road = new int[120][60];
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
	
	public void print(){
		for(int i=0;i<road.length;i++){
			for(int j=0;j<road[i].length;j++){
				int pivot = road[i][j];
				if(Waybill.isBankerWin(pivot)){
					System.out.print("red ");
				} else if(Waybill.isTie(pivot)){
					System.out.print("green ");
				} else if(Waybill.isPlayerWin(pivot)){
					System.out.print("blue ");
				} else {
					System.out.print("X ");
				}
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args){
		StraightRoad way = new StraightRoad();
		way.accept(1).accept(2).getBigEye();
		way.print();
	}
}
