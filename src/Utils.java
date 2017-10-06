
public final class Utils {

	public static boolean emailMatchesPersonObject(String email, PersonObject personObject) {
		// this is the LOCAL version of this method, can make a more strict
		// global version for matching between webpages in future?
		String username = email.split("@")[0].toLowerCase();

		if (username.matches(personObject.mFirstName.toLowerCase() + "\\.?" + personObject.mLastName.toLowerCase())
				|| username.matches(personObject.mFirstName.toLowerCase().charAt(0) + "\\.?" + personObject.mLastName.toLowerCase())) {
			// if username matches johndoe/john.doe@email.com or
			// jdoe/j.doe@email.com format
			return true;
		}

		if (username.matches(personObject.mFirstName.toLowerCase()) || username.matches(personObject.mLastName.toLowerCase())) {
			// if username matches john@email.com or doe@email.com
			return true;
		}

		if (username
				.matches(personObject.mFirstName.toLowerCase() + "\\.?" + personObject.mMiddleInitial.toLowerCase() + "\\.?"
						+ personObject.mLastName.toLowerCase())
				|| username.matches(personObject.mFirstName.toLowerCase() + "\\.?" + personObject.mMiddleInitial.toLowerCase())) {
			// if username matches johntdoe/john.t.doe@email or johnt/john.t@email.com 
			return true;
		}

		return false;
	}

	public static boolean wordInFilter(String word, String[] filter) {
		// check if String is in array of Strings (filter), check for plurals as
		// well
		String theWord = word.toLowerCase();
		boolean flag = false;
		for (String item : filter) {
			if (theWord.equals(item) || theWord.equals(item.concat("s"))) {
				flag = true;
			}
		}
		return flag;
	}
	
	  public static boolean wordInBinarySearchFilter(String word, String[] alphabeticalFilter){
		//special bsearch method for filters arranged alphabetically into an array
		word = word.toLowerCase();
		int bottomIdx = 0;
		int topIdx = alphabeticalFilter.length - 1;
			
		while (bottomIdx <= topIdx){
			int midIdx = (topIdx + bottomIdx) / 2;
			if (alphabeticalFilter[midIdx].equals(word)) {
//				System.out.println("REJECT: keyword in filter: " + alphabeticalFilter[midIdx] + " matches: " + word);
				return true;
			} else if (alphabeticalFilter[midIdx].compareTo(word) > 0) {
				topIdx = midIdx - 1;
			} else if (alphabeticalFilter[midIdx].compareTo(word) < 0) {
				bottomIdx = midIdx + 1;
			}
		}
//		System.out.println("new keyword: " + word);
		return false;
	}

}
