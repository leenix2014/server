package game.task;

public class TaskData implements Comparable<TaskData>{
	public int taskId;
	public String desc;
	public int doneCount;
	public int finishCount;
	public int roomCard;
	public int isGotPrize;
	public int taskType;
	public int order;
	
	@Override
	public int compareTo(TaskData o) {
		if(order > o.order){
			return 1;
		} else if(order < o.order){
			return -1;
		} else {
			return 0;
		}
	}
}
