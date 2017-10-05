import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class Spider {
	public static int waitInterval = 2500;
	
	public static ArrayList<Probe> probeRevolver;
	
	public static boolean exhausted = false; // switch to inform Main whether this Spider has been exhausted
	
	private static LinkedList<String> mOrigins;
	
	public Spider(){
		probeRevolver = new ArrayList<>();
		System.out.println("Thread:" + Thread.currentThread().getId() + "Created new spider");
		initiate();
	}
	
	public void initiate(){
		
		ExecutorService executor = Executors.newFixedThreadPool(Main.coreCount);
		CountDownLatch latch;
		
		while (!exhausted){
			
			while (probeRevolver.size() < 10 && Main.origins.size() > 0){
				probeRevolver.add(new Probe(Main.getOrigin()));
				System.out.println("Thread:" + Thread.currentThread().getId() + " probes: " + probeRevolver.toString());
			}
			
			latch = new CountDownLatch(probeRevolver.size());
			for (byte idx = 0; idx < probeRevolver.size(); idx++){
				Probe thisProbe = probeRevolver.get(idx);
				thisProbe.setLatch(latch);
				executor.execute(thisProbe);
				
//				if (idx == probeRevolver.size() - 1){
//					probeRevolver.remove(idx);
//				}
				
				
			}
			
			try {
				latch.await();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			System.out.println("Latch opened! Size of revolver: " + probeRevolver.size());
			System.out.println("Size of origins LL: " + Main.origins.size());
			
			try {
				Thread.sleep(waitInterval / probeRevolver.size());
			} catch (InterruptedException e){
				e.printStackTrace();
			}
			
			if (probeRevolver.size() < 1){
				//probe array empty even after trying to find new origins to build new probes
				exhausted = true; //spider is done
			}
			
			
		}
		
		executor.shutdown();
	}

}
