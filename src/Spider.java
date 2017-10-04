import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.LinkedList;

public class Spider {
	public static int waitInterval = 3000;
	
	public static HashMap<String, ArrayDeque> unvisitedLinkMap; //hash with base urls as keys and collected urls as values
	public static HashMap<String, HashSet> visitedLinkMap; //hash with base urls as keys and visited urls as values
	
	public static ArrayList<Probe> probeRevolver;
	
	public static boolean exhausted = false; // switch to inform Main whether this Spider has been exhausted
	
	public Spider(LinkedList<String> origins){
		unvisitedLinkMap = new HashMap<>();
		visitedLinkMap = new HashMap<>();
		probeRevolver = new ArrayList<>();
		initiate();
	}
	
	public void initiate(){
		while (!exhausted){
			
			while (probeRevolver.size() < 10 && Main.origins.size() > 0){
				probeRevolver.add(new Probe((String) Main.origins.removeFirst(), this));
			}
			
			for (byte idx = 0; idx < probeRevolver.size(); idx++){
				Probe thisProbe = probeRevolver.get(idx);
				
				try {
					Thread.sleep(waitInterval / probeRevolver.size());
				} catch (InterruptedException e){
					e.printStackTrace();
				}
				System.out.println(thisProbe.origin);
				
				if (idx == probeRevolver.size() - 1){
					probeRevolver.remove(idx);
				}
				
			}
			
			System.out.println("Size of revolver: " + probeRevolver.size());
			System.out.println("Size of origins LL: " + Main.origins.size());
			
			if (probeRevolver.size() < 1){
				//probe array empty even after trying to find new origins to build new probes
				exhausted = true; //spider is done
			}
		}
	}
	
	public static String getNextLink(String origin){
		//retrieve next link of an origin, if ArrayDeque is empty returns null
		//also adds link to visitedLinkMap
		String nextLink = unvisitedLinkMap.get(origin).remove().toString();
		HashSet visitedLinksOfOrigin = visitedLinkMap.get(origin);
		visitedLinksOfOrigin.add(nextLink);
		return nextLink;
	}
	

}
