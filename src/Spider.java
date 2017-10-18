import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class Spider {
	
	public static ArrayList<Probe> probeRevolver;
	
	public static boolean exhausted = false; // switch to inform Main whether this Spider has been exhausted
	
	public Spider(){
		probeRevolver = new ArrayList<>();
		System.out.println("Thread:" + Thread.currentThread().getId() + " INIT " + this.toString());
		initiate();
	}
	
	public void initiate(){
		ExecutorService executor = null;
		CountDownLatch probeLatch;
		
		int waitInterval = 5000;
		int maxThreadLimit = Main.maxThreads;
		if (maxThreadLimit < 2){
			maxThreadLimit = 2;
		}
		
		while (!exhausted){
			long startIteration = System.nanoTime();

			while (probeRevolver.size() < (maxThreadLimit) && Main.origins.size() > 0){
				probeRevolver.add(new Probe(Main.getOrigin()));
				System.out.println("Thread:" + Thread.currentThread().getId() + " probes: " + probeRevolver.toString());
			}
			if (executor != null){
				executor.shutdown();
			}
			executor = Executors.newFixedThreadPool(probeRevolver.size());
			
			probeLatch = new CountDownLatch(probeRevolver.size());
			
			for (Probe thisProbe : probeRevolver){ //execute probes into pool
				thisProbe.setLatch(probeLatch);
				executor.execute(thisProbe);	
			}
			
			try {
				probeLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			try {
				//ensure that enough time has passed between iterations
				int timeNeededToMeetBaseline = (int) (waitInterval - (System.nanoTime() - startIteration)/1000000);
				if (timeNeededToMeetBaseline > 0){
					Thread.sleep(timeNeededToMeetBaseline);
				}
			} catch (InterruptedException e){
				e.printStackTrace();
			}
			
			System.out.println("Latch opened! Size of revolver: " + probeRevolver.size());
			System.out.println("Size of origins LL: " + Main.origins.size());
			
			Iterator<Probe> iterator = probeRevolver.iterator();
			while (iterator.hasNext()){
				if (iterator.next().terminate){
					iterator.remove();
				}
			}
			
			if (probeRevolver.size() < 1){
				//probe array empty even after trying to find new origins to build new probes
				exhausted = true; //spider is done
			}
			
			
		}
		
		executor.shutdown();
	}

}
