import java.util.LinkedList;

public class SpiderThread implements Runnable {
	public static LinkedList<String> mOrigins;
	
	public SpiderThread(LinkedList origins) {
	}


	@Override
	public void run() {
		Spider spider = new Spider();
	}

}
