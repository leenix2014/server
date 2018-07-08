package game.coinroom.util;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class MapUtil {

	/**
	 * 
	 * @param map
	 * @param asc true=从小到大排序  false=从大到小排序
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean asc) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Entry<K, V>> st = map.entrySet().stream();
        st.sorted(
        	new Comparator<Entry<K, V>>() {
	        	@Override
	        	public int compare(Entry<K, V> o1, Entry<K, V> o2) {
	        		if(asc){
	        			return (o1.getValue()).compareTo(o2.getValue());
	        		} else {
	        			return (o2.getValue()).compareTo(o1.getValue());
	        		}
	        	}
        	}
        ).forEach(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }
	
	public static void main(String[] args){
		Map<Integer, Integer> playerCardType = new ConcurrentHashMap<Integer, Integer>();
		playerCardType.put(1, 10);
		playerCardType.put(2, 7);
		playerCardType.put(3, 9);
		playerCardType.put(4, 98);
		playerCardType.put(5, 1000);
		for(Map.Entry<Integer, Integer> entry:sortByValue(playerCardType,false).entrySet()){
			System.out.println(entry.getKey()+","+entry.getValue());
		}
		
		addCompared(666666, 777777);
		System.out.println(compared(777777, 666666));
		System.out.println(compared(777776, 666667));
	}
	
	private static class Couple{
		private int player1;
		private int player2;
		public Couple(int player1, int player2){
			this.player1 = player1;
			this.player2 = player2;
		}
		@Override
		public int hashCode() {
	        return player1 + player2;
		}
		@Override
		public boolean equals(Object anObject) {
			if (this == anObject) {
	            return true;
	        }
	        if (anObject instanceof Couple) {
	        	Couple other = (Couple)anObject;
	            if((this.player1 == other.player1 && this.player2 == other.player2)
	            		|| (this.player2 == other.player1 && this.player1 == other.player2)){
	            	return true;
	            }
	        }
	        return false;
		}
	}
	private static Set<Couple> comparedCouple = new HashSet<>();
	
	private static boolean compared(int player1, int player2){
		if(player1 == player2){
			return true;
		}
		return comparedCouple.contains(new Couple(player1, player2));
	}
	
	private static void addCompared(int player1, int player2){
		comparedCouple.add(new Couple(player1, player2));
	}
}
