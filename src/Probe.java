import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Probe {
	public WebClient webClient;
	public String origin;
	public static boolean disfunctional = false;
	public static Spider associatedSpider;
	
	public Probe(String inputOrigin, Spider spider){
		//probe begins at origin url, stores an instance of webClient, 
		origin = inputOrigin;
		associatedSpider = spider;
		
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
			disfunctional = true;
			webClient.close();
			e.printStackTrace();
		}
		
		extractFrom(origin); //do initial pull
	}
	
	public boolean extractFrom(String url){
		try {
			String response = webClient.getPage(url).getWebResponse().getContentAsString();
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

}
