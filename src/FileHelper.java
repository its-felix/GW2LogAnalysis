import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

/*
 * Contains class's/functions to assist in parsing/reading files. 
 */
public class FileHelper {


	public static String getFileCount(File directoryFile, String suffix) {
		var total=Arrays.asList(directoryFile.list())
                .stream()
                .filter(x -> x.contains(suffix))
                .collect(Collectors.counting());
        return total.toString();
	}
	
	
	public static String getFileExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
		if (i > p) {
		    return fileName.substring(i+1);
		}
		return "";
	}
	
	public static String getFileName(String fileLoc) {
		Path p = Paths.get(fileLoc);
		String fileName = p.getFileName().toString();
		int i = fileName.lastIndexOf('.'); //remove file extension.
		
		return fileName.substring(0, i);
	}
}
