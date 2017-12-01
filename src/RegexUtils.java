import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
	
	static Pattern absPattern = Pattern.compile("^https?.+", Pattern.CASE_INSENSITIVE);
	static Pattern subdirPattern = Pattern.compile("[a-zA-Z0-9/\\-_\\.&'%]+", Pattern.CASE_INSENSITIVE);
	static Pattern rootPattern = Pattern.compile("^[a-z0-9_=\\?^/]\\.html", Pattern.CASE_INSENSITIVE);
	
	public static HashSet<String> findEmails(String input) {
		HashSet<String> emailsDiscovered = new HashSet<>();
		// split input into array using delimiter of unlimited whitespace to
		String[] splitWordArray = input.split("\\s+");
		for (String token : splitWordArray) {
			if (token != null && token != "") {
				//compile email regex
				Pattern pattern = Pattern.compile("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,8}(\\.[A-Z]{2,8})?",
						Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(token);
				if (matcher.find()) {
					token = matcher.group();
					if (!unwantedUrlDestination(token)){
						emailsDiscovered.add(token.toLowerCase());
//						System.out.println("RegexUtils discovered email: " + token);
					}
				}
			}
		}
		// turn the hash set into an array of strings
		return emailsDiscovered;
	}

	public static boolean urlDomainNameMatch(String urlA, String urlB) {
		// check if the host names of two urls match, ignore www part

		String hostA = "";
		try {
			URL builtUrlA = new URL(urlA);
			hostA = builtUrlA.getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		String hostB = "";
		try {
			URL builtUrlB = new URL(urlB);
			hostB = builtUrlB.getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// now extract substring from strings, domain name, to take care of www
		String siteNameA = "";
		// search for a pattern like "google.com" in
		// http://www.google.com/maps using RegEx
		Pattern patternA = Pattern.compile("([^\\.]+)\\.(co.)?([^\\.]+)$");
		Matcher matcherA = patternA.matcher(hostA);
		if (matcherA.find()) {
			siteNameA = matcherA.group();
		}

		String siteNameB = "";
		// search for the pattern I want, like "google.com/" in
		// http://www.google.com/maps
		Pattern patternB = Pattern.compile("([^\\.]+)\\.(co.)?([^\\.]+)$");
		Matcher matcherB = patternB.matcher(hostB);
		if (matcherB.find()) {
			siteNameB = matcherB.group();
		}
		return siteNameA.equals(siteNameB);
	}

	public static boolean unwantedUrlDestination(String url) {
		if (url.matches(".+(.jpg|.jpeg|.png|.gif|.pdf|.svg|.stm|#|.xml|.json|.vcf|.js|.oxps|.mp4|.mp3|.csv|.docx|.doc|.zip){1}$")
				|| url.matches("^(mailto:|tel:|redirect){1}.+") || url.startsWith("#")) {
			return true;
		}
		return false;
	}
	

}
