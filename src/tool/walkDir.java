package tool;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;


public class walkDir {
	public static void main(String[] args){
		new walkDir().walk("src/test/");
	}
	/**
	 * parameter is the path
	 * return all the java files
	 */
	public List<File> walk(String path){
		File dir = new File(path);
		Collection<File> files = FileUtils.listFiles(dir, null, true);
		List<File> javafiles = new ArrayList<File>();
		String extension;
		for(File file :files){
			extension = FilenameUtils.getExtension(file.getName()) ;
			if(extension.equals("java"))
				javafiles.add(file);
		}
		return javafiles;
	}
}
