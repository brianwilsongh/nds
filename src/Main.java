import java.util.ArrayList;

public class Main {
	static ArrayList links; //links to websites to iterate over
    
	public static void main(String[] args){
	    // turn off htmlunit warnings
	    java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
	    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
		System.out.println("Hello world");
		
		links = IOUtils.getLinks();
		Spider spider = new Spider(links);
		System.out.println(spider.toString());
		System.out.println(links);
	}

}
