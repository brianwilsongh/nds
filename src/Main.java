import java.util.LinkedList;
import java.util.ArrayList;

public class Main {
	volatile static LinkedList origins; //links to websites to iterate over
	static ArrayList<Spider> spiders = new ArrayList<>();
    
	public static void main(String[] args){
	    // turn off htmlunit warnings
		doConfiguration();
		
		origins = IOUtils.getLinks();
		
		Spider spider = new Spider(origins);
		spiders.add(spider);
		
		System.out.println(spider.toString());
		System.out.println(origins);
		
		
		while (!allLinksExhausted()){
		}
	}
	
	private static boolean allLinksExhausted(){
		for (Spider thisSpider : spiders){
			if (thisSpider.exhausted){
				
			}
		}
		return false;
	}
	
	private static void doConfiguration(){
	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
	    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
	    int coreCount = Runtime.getRuntime().availableProcessors();
//	    System.out.println(coreCount + " cores available");
	}

}
