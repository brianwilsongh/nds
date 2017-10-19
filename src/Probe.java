import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Probe implements Runnable {
	//probes are threads run by spider - keeps track of position within domain tree, 1 per domain
	public WebClient webClient;
	public String origin;
	public boolean terminate = false;
	
	public CountDownLatch mLatch;
	
	private Deque<String> unvisitedLinks; //hash with base urls as keys and collected urls as values
	private Set<String> visitedLinks; //hash with base urls as keys and visited urls as values
	private Set<String> extractedEmails;
	
	public Probe(String inputOrigin){
		//probe begins at origin url, stores an instance of webClient, extracts initial
		origin = inputOrigin;
		unvisitedLinks = new ArrayDeque<>();
		visitedLinks = new HashSet<>();
		extractedEmails = new HashSet<>();
		
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
			System.out.println("Probe for " + inputOrigin + " failed to initiate webClient!");
			webClient.close();
			e.printStackTrace();
		}
		
		System.out.println("Thread: " + Thread.currentThread().getId() + "Initiated probe for " + origin);
		extractFrom(origin); //do initial pull to start building queue
	}
	
	@Override
	public void run() {
		//this method retrieves response of next link, terminate if no more links
		
		try {
			extractFrom((String) unvisitedLinks.remove());
		} catch (NoSuchElementException e){
			System.out.println("Set terminate=true for " + origin);
			mLatch.countDown();
			this.terminate = true;
		}

		IOUtils.writeLineToStream("Thread " + Thread.currentThread().getId() + " probe runs: " + origin);
		mLatch.countDown();
		
		int randKill = (int) Math.ceil(Math.random() * 100); //TEST only, pretend probe ran out 1/20 chance
			if (randKill > 90){
				System.out.println("RandomTermination: " + origin);
				this.terminate = true;
			}
	}
	
	public void setLatch(CountDownLatch newLatch){
		mLatch = newLatch;
	}
	
	private void extractFrom(String url){
		//gets http response, pulls contacts/links
		System.out.println("url is " + url + "for probe with origin " + origin);
		if (!(url.length() > 0)){
			return;
		}
		try {
			String response = webClient.getPage(url).getWebResponse().getContentAsString();
			pullContacts(response);
			//TODO: Pull Links, add methods and necessary network utils
			if (response.length() == 0){
				System.out.println(this.toString() + " received no response from " + url);
			}
		} catch (Exception e){
			System.out.println("Failed fetch, probe " + this.toString() + ", thread " + Thread.currentThread().getId() + ", for url " + url);
			e.getMessage();
		}
	}
	
	private void pullContacts(String source) {
		HashSet<String> emailSet = RegexUtils.findEmails(source);
		if (emailSet.size() > 0) {
			for (String emailItem : emailSet) {
				if (!extractedEmails.contains(emailItem)) {
					try {
						IOUtils.writeLineToStream(emailItem + ",");
						extractedEmails.add(emailItem);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}
	}
	
	
	
	
		

}
