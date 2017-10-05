import java.util.LinkedList;
import java.util.ArrayList;

public class Main {
	volatile static LinkedList origins; //links to websites to iterate over
	static ArrayList<SpiderThread> spiderThreads = new ArrayList<>();
	
	static int coreCount; //num of cores availabe
    
	public static void main(String[] args){
	    // turn off htmlunit warnings
		configure();
		
		Spider spider = new Spider();

		System.out.println("Reached the end of Main.class");
		
		//call start on all threads then join to pause until they finish
		
	}
	
	public static synchronized String getOrigin(){
		String origin = (String) origins.removeFirst();
		return origin;
	}
	
	private static void configure(){
	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
	    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
	    coreCount = Runtime.getRuntime().availableProcessors();
//	    System.out.println(coreCount + " cores available");
	    origins = IOUtils.getLinks();
	}

}
