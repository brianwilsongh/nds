import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class Main {
	volatile static LinkedList<String> origins; //links to websites to iterate over
	
	static int maxThreads;
	static PrintStream printStream = System.out; //will refactor to be socket printstream
	

	static PrintWriter printWriter;
	static BufferedWriter bufferedWriter;
	static {
		try {
			bufferedWriter = new BufferedWriter(new FileWriter("output.csv"));
			printWriter = new PrintWriter(bufferedWriter);
		} catch (Exception e) {
			e.printStackTrace();
		} //TODO: move to stream when prepared for server
	}

    
	public static void main(String[] args){
		configure();
		
		CountDownLatch spiderLatch = new CountDownLatch(1);
		Spider spider = new Spider(spiderLatch); //construction will autorun
		
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
	    if (maxThreads < 7) maxThreads = 7;
	    origins = IOUtils.getLinks();
	}

}

