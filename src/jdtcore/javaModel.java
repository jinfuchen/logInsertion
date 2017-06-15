package jdtcore;
import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.CoreException;

import tool.walkDir;


public class javaModel {
	//public final static String javaFilePath = "E:\\Workspaces\\eclipse\\MyLab\\src\\book\\bytecode.java";
	//the path is the top root of haddop project
	public final static String javaFilePath = "hadoop/";
	//public final static String javaFilePath = "src/test/";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			walkDir walkdir = new walkDir();
			List<File> files = walkdir.walk(javaFilePath);
			for(File file: files){
				demoVisitor(file);
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void demoVisitor(File file) throws CoreException, Exception {

        JdtAst jdt = new JdtAst();
        System.out.println("file path:\t"+file.getAbsolutePath());
        jdt.getNewCompilationUnit(file);//
	}
}

