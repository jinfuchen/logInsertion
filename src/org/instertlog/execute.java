package org.instertlog;

import java.io.File;
import java.util.List;

import org.core.JavaToAst;
import org.eclipse.core.runtime.CoreException;
import org.util.Constant;
import org.util.filterFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class execute {
	private static Logger logger = LoggerFactory.getLogger(execute.class);
	
	public static void main(String[] args) throws CoreException, Exception {
		// TODO Auto-generated method stub
		logger.info("inserting log into method......");
		filterFile walkdir = new filterFile();
		List<File> files = walkdir.walk(Constant.TESTFILE);
		for(File file: files){
			JavaToAst jdt = new JavaToAst();
	        logger.info("file path: {}",file.getAbsolutePath());
	        jdt.getCompilationUnit2(file);
		}
	}
}
