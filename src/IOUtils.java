import java.util.HashMap;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class IOUtils {
	
	public static ArrayList<String> getLinks(){
		ArrayList<String> links = new ArrayList<>();
		try {
			Files.lines(Paths.get(System.getProperty("user.dir") + "/target.txt")).forEachOrdered(link->links.add(link.toLowerCase()));;
		} catch (Exception e){
			e.printStackTrace();
		}
		return links;
	}
	

}
