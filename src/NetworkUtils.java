import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.XHtmlPage;

public class NetworkUtils {

	public static URL makeURL(String string, String origin) {
		URL returnURL = null;
		try {
			// make the URL out of the string
			returnURL = new URL(origin + string);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("NUtils.makeURL failed to make url from: " + string + " added onto origin: " + origin);
		}
		return returnURL;
	}

	public static String insertWebSubdomian(String theURL) {
		if (theURL.matches("(http://){1}(www){0}[^(www)]+")) {
			System.out.println("NetworkUtils insertWebSubdomain found no www subdomain, fixing this url: " + theURL);
			StringBuilder temp = new StringBuilder(theURL);
			temp.insert(7, "www.");
			System.out.println("NetworkUtils" + " newly fixed: " + temp.toString());
			theURL = temp.toString();
		}

		if (theURL.matches("(https://){1}(www){0}[^(www)]+")) {
			System.out.println("NetworkUtils" + " makeURL found no www subdomain, fixing this url: " + theURL);
			StringBuilder temp = new StringBuilder(theURL);
			temp.insert(7, "www.");
			System.out.println("NetworkUtils newly fixed: " + temp.toString());
			theURL = temp.toString();
		}

		return theURL;
	}

	public static boolean urlHostMatch(URL urlA, URL urlB) {
		// check if the paths match of built URL objects
		try {
			// build a url to make string for A, then B
			String protocolA = urlA.getProtocol();
			String protocolB = urlB.getProtocol();

			String hostA = urlA.getHost();
			String hostB = urlB.getHost();
			// strip the www. out of the host A/B if it exists
			if (hostA.substring(0, 4).equals("www.")) {
				hostA = hostA.substring(4, hostA.length());
			}
			if (hostB.substring(0, 4).equals("www.")) {
				hostB = hostB.substring(4, hostB.length());
			}

			String finalizedA = protocolA + hostA;

			if (finalizedA.equals(protocolB + hostB)) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String getOrigin(String urlA) {
		// eg 'http://www.example.com'
		URL theBuiltUrl = makeURL(urlA, null);
		return theBuiltUrl.getProtocol() + theBuiltUrl.getHost();
	}
	
}
