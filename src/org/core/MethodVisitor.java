package org.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;

import java.io.File;
import java.io.IOException;


public class MethodVisitor extends ASTVisitor{
	private Document document;
	private File file;
	private InsertLog insertLog;
	
	public MethodVisitor(Document document, File file) {
		// TODO Auto-generated constructor stub
		this.document = document;
		this.file = file;
		insertLog = new InsertLog();
	}
	
	
	//visit each method, insert log 
//	@SuppressWarnings("unchecked")
//	@Override
//	public boolean visit(MethodDeclaration node) {
//		try {
////			insertLog.methodAddprints(methodDec, document, file);
//			insertLog.methodAddprints2(node);
//		} //catch (MalformedTreeException | BadLocationException | CoreException | IOException e) {
//			catch(MalformedTreeException e){
//			e.printStackTrace();
//		}
//		return true;		
//	}

	@Override
	public boolean visit(CompilationUnit node) {
		// TODO Auto-generated method stub
		try {
			insertLog.fileAddprints2(node, document, file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return super.visit(node);
		
	}
	
}
