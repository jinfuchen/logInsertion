package org.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;


public class filterFile {
	public static void main(String[] args){ //test function walk
		new filterFile().walk("src/test/");
	}
	/**
	 * parameter is the path: string
	 * filter java files, exclude test java files
	 */
	public List<File> walk(String path){
		File dir = new File(path);
		Collection<File> files = FileUtils.listFiles(dir, null, true);
		List<File> javafiles = new ArrayList<File>();
		String extension;
		for(File file :files){
			extension = FilenameUtils.getExtension(file.getName()) ;
//			if(extension.equals("java") && file.getName().indexOf("Test")==-1)
			if(extension.equals("java"))
				javafiles.add(file);
		}
		return javafiles;
	}
	

}
