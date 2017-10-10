import java.util.HashMap;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public class IOUtils {
	
	public static LinkedList<String> getLinks(){
		LinkedList<String> links = new LinkedList<>();
		try {
			Files.lines(Paths.get(System.getProperty("user.dir") + "/target.txt")).forEachOrdered(link->links.add(link.toLowerCase()));;
		} catch (Exception e){
			e.printStackTrace();
		}
		return links;
	}
	
	public static synchronized void writeToOutput(File file, String s){
		//will be called from multiple threads to write to output file in main
	}
	

}
