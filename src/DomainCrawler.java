import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class DomainCrawler {
	public WebClient webClient;
	public String origin;
	
	public DomainCrawler(String inputOrigin){
		//an object that begins at origin url, method .extract() return boolean, true if visited page successfully & was within domain
		origin = inputOrigin;
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
			e.getMessage();
		}
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
