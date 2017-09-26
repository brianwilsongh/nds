import java.util.HashMap;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.LinkedList;

public class Spider {
	//class to store and work with the domain crawlers
	public HashMap<String, ArrayDeque> unvisitedLinkMap; //hash with base urls as keys and collected urls as values
	public HashMap<String, LinkedList> visitedLinkMap; //hash with base urls as keys and visited urls as values
	
	public Spider(ArrayList<String> links){
		unvisitedLinkMap = new HashMap<>();
		visitedLinkMap = new HashMap<>();
		for (int idx = 0; idx < links.size(); idx++){
			try {
				unvisitedLinkMap.put(links.get(idx).toString(), new ArrayDeque());
				visitedLinkMap.put(links.get(idx).toString(), new LinkedList());
			} catch (NullPointerException e){
				e.printStackTrace();
			}
		}
	}
	

}
