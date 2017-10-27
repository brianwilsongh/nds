import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Probe implements Runnable {
	// probes are threads run by spider - keeps track of position within domain
	// tree, 1 per domain
	public WebClient webClient;
	public String origin;
	public URL originObj;
	public boolean terminate = false;

	private CountDownLatch mLatch;

	private Deque<String> linkQueue;
	private Set<String> discoveredLinks;
	private Set<String> extractedEmails;
	
	String DEFAULT_USER_AGENT_STRING = "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:24.0) Gecko/20100101 Firefox/24.0";
	
	private int ctr = 0;

	public Probe(String inputOrigin) {
		// start crawl @ origin, stores instance of webClient
		linkQueue = new ArrayDeque<>(); // queue of urls
		discoveredLinks = new HashSet<>(); // discovered urls in set
		extractedEmails = new HashSet<>(); //discovered emails in set
		
		origin = inputOrigin;
		if (origin.endsWith("/")) {
			origin = origin.substring(0, origin.length() - 1);
		}
		discoveredLinks.add(origin);


		try {
			webClient = new WebClient();
			webClient.getBrowserVersion().setUserAgent(DEFAULT_USER_AGENT_STRING);
			WebClientOptions options = webClient.getOptions();
			options.setThrowExceptionOnFailingStatusCode(false);
			options.setUseInsecureSSL(true);
			options.setThrowExceptionOnScriptError(false);
			options.setJavaScriptEnabled(false);
			options.setCssEnabled(true);
			options.setDownloadImages(false);
			options.setPopupBlockerEnabled(true);
			options.setGeolocationEnabled(false);
			options.setTimeout(7000);
			originObj = new URL(origin);
		} catch (Exception e) {
			terminate = true;
			System.out.println("Probe for " + inputOrigin + " failed to initiate webClient!");
			webClient.close();
			e.printStackTrace();
		}

		System.out.println("Thread:" + Thread.currentThread().getId() + " Initiated probe for " + origin);
		extractFrom(origin); // do initial pull to start building queue
	}

	@Override
	public void run() {
		// this method retrieves response of next link, terminate if no more
		// links

		try {
			extractFrom((String) linkQueue.remove());
		} catch (NoSuchElementException e) {
			System.out.println("Set terminate=true for " + origin);
			mLatch.countDown();
			this.terminate = true;
		}

		// IOUtils.writeLineToStream("Thread" + Thread.currentThread().getId() +
		// " probe runs: " + origin);
		mLatch.countDown();

		int randKill = (int) Math.ceil(Math.random() * 100); // TEST only,
																// pretend probe
																// ran out 1/20
																// chance
//		if (randKill > 90) {
//			System.out.println("RandomTermination: " + origin);
//			this.terminate = true;
//		}
	}

	public void setLatch(CountDownLatch newLatch) {
		mLatch = newLatch;
	}

	private void extractFrom(String url) {
		// gets http response, pulls contacts/links
		System.out.println(origin + " visit:" + ctr++ + " discovered:" + discoveredLinks.size() + " in queue:" + linkQueue.size());
		System.out.println(this.toString() + " extracting from: " + url);
		if (!(url.length() > 0)) {
			return;
		}
		try {
			HtmlPage htmlPage = webClient.getPage(url);
			pullLinks(htmlPage);
			pullContacts(htmlPage.getWebResponse().getContentAsString());
			if (htmlPage.getWebResponse().getContentAsString().length() == 0) { // kill
																				// in
																				// production
				System.out.println(this.toString() + " received no response from " + url);
			}
		} catch (Exception e) {
			System.out.println("Failed fetch, probe " + this.toString() + ", thread " + Thread.currentThread().getId()
					+ ", for url " + url);
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

	private void pullLinks(HtmlPage htmlPage) {
		List<HtmlAnchor> anchors = htmlPage.getAnchors();
		for (HtmlAnchor anchor : anchors) {
			// TODO: maybe add functionality to follow image within <a> tag
			String path = anchor.getHrefAttribute();
			if (path.length() > 0 && !RegexUtils.unwantedUrlDestination(path)) {
				//visible link with href that doesn't lead to file

				Pattern absPattern = Pattern.compile("^https?.+", Pattern.CASE_INSENSITIVE);
				Matcher absoluteMatcher = absPattern.matcher(path);
				if (absoluteMatcher.find() && !discoveredLinks.contains(path)) { // abs url
					URL pathObj;
					try {
						pathObj = new URL(path);
						if (NetworkUtils.urlHostMatch(originObj, pathObj)) {
							linkQueue.add(path);
							discoveredLinks.add(path);
							System.out.println("new abs link: " + path);
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				} else if (!discoveredLinks.contains(path)) { // rel url
					Pattern subdirPattern = Pattern.compile("^\\/(?!\\/).+", Pattern.CASE_INSENSITIVE);
					Matcher subdirMatch = subdirPattern.matcher(path);
					Pattern rootPattern = Pattern.compile("^[a-z0-9_]\\.html", Pattern.CASE_INSENSITIVE);
					Matcher rootMatch = rootPattern.matcher(path);
					String builtPath = "";
					if (subdirMatch.find()) {
						builtPath = NetworkUtils.makeURL(path, origin).toString();
					} else if (rootMatch.find()) {
						builtPath = origin + "/" + path;
					}
					if (!discoveredLinks.contains(builtPath) && builtPath.length() > 0) {
						System.out.println("new rel link: " + builtPath);
						linkQueue.add(builtPath);
						discoveredLinks.add(builtPath);
					}
				}
			}

		}
	}

}
