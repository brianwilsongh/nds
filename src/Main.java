import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.io.PrintStream;
import java.util.ArrayList;

public class Main {
	volatile static LinkedList<String> origins; //links to websites to iterate over
	
	static int maxThreads;
	static PrintStream printStream = System.out; //will refactor to be socket printstream
    
	public static void main(String[] args){
		configure();
		
		System.out.println("Free memory: " + Runtime.getRuntime().freeMemory());
		System.out.println("Max thread cap : " + maxThreads);
		
		Spider spider = new Spider(); //construction will autorun
		
		System.out.println("-- JAR TERMINATED --");

	}
	
	
	public static synchronized String getOrigin(){
		String origin = (String) origins.removeFirst();
		return origin;
	}
	
	private static void configure(){
	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
	    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
//	    coreCount = Runtime.getRuntime().availableProcessors();
	    maxThreads = (int) Runtime.getRuntime().freeMemory() / 6291456; // threads based on 6mb reserved per thread
	    origins = IOUtils.getLinks();
	}

}

