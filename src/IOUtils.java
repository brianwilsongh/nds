import java.io.IOException;
import java.nio.file.Files;
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
	
	public static synchronized void writeLineToStream(String s){
		//will be called from multiple threads to write to output file in main
		Main.printWriter.println(s);
		try {
			Main.bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Main.printStream.println("OUTPUTSTREAM:: " + s);
	}
	

}