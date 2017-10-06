import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Probe implements Runnable {
	public WebClient webClient;
	public String origin;
	public static boolean terminate = false;
	
	public static CountDownLatch mLatch;
	
	public static ArrayDeque unvisitedLinks; //hash with base urls as keys and collected urls as values
	public static HashSet visitedLinks; //hash with base urls as keys and visited urls as values
	
	public Probe(String inputOrigin){
		//probe begins at origin url, stores an instance of webClient, 
		origin = inputOrigin;
		unvisitedLinks = new ArrayDeque();
		visitedLinks = new HashSet<>();
		
		try {
			webClient = new WebClient();
			WebClientOptions options = webClient.getOptions();
			options.setThrowExceptionOnFailingStatusCode(false);
			options.setUseInsecureSSL(true);
			options.setThrowExceptionOnScriptError(false);
			options.setJavaScriptEnabled(false);
			options.setCssEnabled(false);
			options.setDownloadImages(false);
			options.setPopupBlockerEnabled(true);
			options.setGeolocationEnabled(false);
			options.setTimeout(7000);
		} catch (Exception e){
			terminate = true;
			webClient.close();
			e.printStackTrace();
		}
		
		System.out.println("Thread:" + Thread.currentThread().getId() + "Initiated probe for " + origin);
		extractFrom(origin); //do initial pull
	}
	
	public boolean extractFrom(String url){
		try {
			String response = webClient.getPage(url).getWebResponse().getContentAsString();
			System.out.println("response: " + response);
			if (response.length() > 0){
				return true;
			} else {
				return false;
			}
		} catch (Exception e){
			e.getMessage();
			return false;
		}
	}
	
	public static void setLatch(CountDownLatch newLatch){
		mLatch = newLatch;
	}

	@Override
	public void run() {
		//this method retrieves resposne of next link, terminate if no more links
		try {
			long sleep = (long) (Math.random() * 4000); //simulate network latency
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Thread " + Thread.currentThread().getId() + " probe run: " + origin);
		
		mLatch.countDown();
	}

}
